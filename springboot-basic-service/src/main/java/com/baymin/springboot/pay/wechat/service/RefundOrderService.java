package com.baymin.springboot.pay.wechat.service;

import com.baymin.springboot.pay.wechat.param.Configure;
import com.baymin.springboot.pay.wechat.param.Util;
import com.baymin.springboot.pay.wechat.param.WXResCommonData;
import com.baymin.springboot.pay.wechat.param.refundorder.RefundOrderReqData;
import com.baymin.springboot.pay.wechat.param.refundorder.RefundOrderResData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefundOrderService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(RefundOrderService.class);

    public RefundOrderService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(Configure.ORDER_REFUND_API);
    }

    /**
     * 退款申请业务处理
     *
     * @param reqData
     * @param resultListener
     * @throws Exception
     */
    public void doService(RefundOrderReqData reqData, RefundOrderService.ResultListener resultListener) throws Exception {
        String responseStr;

        long costTimeStart = System.currentTimeMillis();
        responseStr = sendPost(reqData);
        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;

        log.info("refundOrder request consume：" + totalTimeCost + "ms");
        log.info("unifiedorder response：" + responseStr);

        try {
            RefundOrderResData resData = (RefundOrderResData) Util.getObjectFromXML(responseStr, RefundOrderResData.class);
//			UnifiedOrderResData resData = new UnifiedOrderResData();
            resData.setReturn_code(WXResCommonData.SUCCESS_RETURN_CODE);
            resData.setResult_code(WXResCommonData.SUCCESS_RESULT_CODE);
            resData.setOut_trade_no("TEST-10023129329435");
            // 判断通信标识return_code
            if (resData == null || resData.getReturn_code() == null) {
                log.error("订单退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                resultListener.onFailByReturnCodeError(resData);
                return;
            }
            if (WXResCommonData.FAIL_RETURN_CODE.equals(resData.getReturn_code())) {
                log.error("订单退款API系统返回失败，请检测Post给API的数据是否规范合法");
                resultListener.onFailByReturnCodeFail(resData);
                return;
            }
            // 判断业务结果result_code
            if (WXResCommonData.SUCCESS_RETURN_CODE.equals(resData.getReturn_code())) {
                if (WXResCommonData.FAIL_RESULT_CODE.equals(resData.getResult_code())) {
                    log.error("订单退款API系统返回失败");
                    resultListener.onUnifiedOrderFail(resData);
                    return;
                }
                if (WXResCommonData.SUCCESS_RESULT_CODE.equals(resData.getResult_code())) {
                    log.info("订单退款API系统成功返回数据");
                    resultListener.onUnifiedOrderSuccess(resData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单退款API调用失败");
        }
    }

    public interface ResultListener {
        /**
         * API返回ReturnCode不合法
         *
         * @param resData
         */
        void onFailByReturnCodeError(RefundOrderResData resData);

        /**
         * API返回ReturnCode为FAIL时回调
         *
         * @param resData
         */
        void onFailByReturnCodeFail(RefundOrderResData resData);

        /**
         * 通信成功，但业务结果失败时回调
         *
         * @param resData
         */
        void onUnifiedOrderFail(RefundOrderResData resData);

        /**
         * 通信成功且业务结果成功时回调
         *
         * @param resData
         */
        void onUnifiedOrderSuccess(RefundOrderResData resData);
    }

}
