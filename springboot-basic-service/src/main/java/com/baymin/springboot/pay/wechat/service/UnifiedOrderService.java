package com.baymin.springboot.pay.wechat.service;

import com.baymin.springboot.pay.wechat.param.Configure;
import com.baymin.springboot.pay.wechat.param.Util;
import com.baymin.springboot.pay.wechat.param.WXResCommonData;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderReqData;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnifiedOrderService extends BaseService {
	
	private static Logger log = LoggerFactory.getLogger(UnifiedOrderService.class);

	public UnifiedOrderService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		super(Configure.UNIFIEDORDER_API);
	}
	
	/**
	 * 统一下单业务处理
	 * @param reqData
	 * @param resultListener
	 * @throws Exception
	 */
	public void doService(UnifiedOrderReqData reqData, ResultListener resultListener) throws Exception {
		String responseStr;

		long costTimeStart = System.currentTimeMillis();
		responseStr = sendPost(reqData);
		long costTimeEnd = System.currentTimeMillis();
		long totalTimeCost = costTimeEnd - costTimeStart;
		
		log.info("unifiedorder request consume：" + totalTimeCost + "ms");
		log.info("unifiedorder response：" + responseStr);

		try {
			UnifiedOrderResData resData = (UnifiedOrderResData) Util.getObjectFromXML(responseStr, UnifiedOrderResData.class);
			// 判断通信标识return_code
			if(resData == null || resData.getReturn_code() == null) {
				log.error("统一下单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
				resultListener.onFailByReturnCodeError(resData);
				return;
			}
			if(WXResCommonData.FAIL_RETURN_CODE.equals(resData.getReturn_code())) {
				log.error("统一下单API系统返回失败，请检测Post给API的数据是否规范合法");
				resultListener.onFailByReturnCodeFail(resData);
				return;
			}
			// 判断业务结果result_code
			if(WXResCommonData.SUCCESS_RETURN_CODE.equals(resData.getReturn_code())) {
				if(WXResCommonData.FAIL_RESULT_CODE.equals(resData.getResult_code())) {
					log.error("统一下单API系统返回失败");
					resultListener.onUnifiedOrderFail(resData);
					return;
				}
				if(WXResCommonData.SUCCESS_RESULT_CODE.equals(resData.getResult_code())) {
					log.info("统一下单API系统成功返回数据");
					resultListener.onUnifiedOrderSuccess(resData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("统一下单API调用失败");
		}
	}
	
	public interface ResultListener {
		/**
		 * API返回ReturnCode不合法
		 * @param resData
		 */
		void onFailByReturnCodeError(UnifiedOrderResData resData);

		/**
		 * API返回ReturnCode为FAIL时回调
		 * @param resData
		 */
		void onFailByReturnCodeFail(UnifiedOrderResData resData);

		/**
		 * 通信成功，但业务结果失败时回调
		 * @param resData
		 */
		void onUnifiedOrderFail(UnifiedOrderResData resData);

		/**
		 * 通信成功且业务结果成功时回调
		 * @param resData
		 */
		void onUnifiedOrderSuccess(UnifiedOrderResData resData);
	}

}