package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderReqData;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import com.baymin.springboot.pay.wechat.service.UnifiedOrderService;
import com.baymin.springboot.service.IPayRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付记录service
 * Created by baymin on 2016/9/12.
 */
@Service
@Transactional
public class PayRecordServiceImpl implements IPayRecordService {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private PayRecordDao payRecordDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartDAO shoppingCartDAO;
    @Autowired
    private ProductPackageDAO productPackageDAO;
    @Autowired
    private UserOrderDAO userOrderDAO;
    @Autowired
    private ProductPackageSettingService productPackageSettingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductPackageService productPackageService;

    @Override
    public Map<String, Object> payOrderWithWeChat(final User user, List<Order> orderList, String appID, String mchID, String key, final String orderId, final String tradeId, String openId, Double wechatFee) {
        final Map<String, Object> reMap = new HashMap<>();

        final double totalFee = wechatFee;
        double totalProductFee = FormatOrder.getPayPriceByOrders(orderList);

        String channelCode;
        if (StringUtils.isNotBlank(tradeId)) {
            channelCode = "TRADE";
        } else {
            channelCode = "MALL";
        }

        try {
            final UnifiedOrderReqData reqData = new UnifiedOrderReqData(null, "JSAPI", orderList.get(0).getGoodsName(), (int) (totalFee * 100), appID, mchID, key, channelCode, openId);
            final Double finalTotalBalance = BigDecimalUtil.sub(totalProductFee, totalFee);
            new UnifiedOrderService().doService(reqData, new UnifiedOrderService.ResultListener() {
                @Override
                public void onFailByReturnCodeError(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, "统一下单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                }

                @Override
                public void onFailByReturnCodeFail(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, resData.getReturn_msg()+"!!");
                }

                @Override
                public void onUnifiedOrderFail(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, resData.getErr_code_des()+"!!!");
                }

                @Override
                public void onUnifiedOrderSuccess(UnifiedOrderResData resData) {
                    // 在系统生成预支付订单
                    PayRecord payRecord = new PayRecord();
                    payRecord.setAccount(user.getAccount());
                    payRecord.setCreateTime(System.currentTimeMillis());
                    payRecord.setPayFee(totalFee);
                    payRecord.setBalance(finalTotalBalance);
                    payRecord.setPayResult(false);
                    payRecord.setPayWay(PayWay.PAY_ONLINE_WITH_BALANCE_WECHAT);
                    payRecord.setPayFor(PayFor.PACKAGE_ORDER);
                    payRecord.setResultDesc("生成预支付订单");
                    payRecord.setTradeId(tradeId);
                    payRecord.setOrderId(orderId);
                    payRecord.setTradeNo(reqData.getOut_trade_no());
                    payRecord.setCanceled(false);
                    payRecordDao.add(PayRecord.class, payRecord);

                    // 扣除用户余额
                    if (finalTotalBalance > 0) {
                        BalanceFlow balanceFlow = new BalanceFlow(payRecord.getAccount(), FlowType.OUTFLOW, FlowSource.BALANCE_ORDER,
                                finalTotalBalance, "用户余额支付商品价", new Date(), payRecord.getId());
                        userService.addBalanceFlow(balanceFlow);
                    }

                    resData.setOut_trade_no(reqData.getOut_trade_no());

                    reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
                    reMap.put(WebConstant.INFO, resData);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "生成预付单失败");
        }

        return reMap;
    }

    @Override
    public void orderPaySuccess(PayRecord payRecord) throws Exception {
        List<Order> orderList = new ArrayList<>();
        if (StringUtils.isNotBlank(payRecord.getTradeId())) {
            orderList = orderDAO.getOrdersByTradeId(payRecord.getTradeId());
        } else {
            orderList.add(orderDAO.find(Order.class, payRecord.getOrderId()));
        }

        Double totalPrice = FormatOrder.getPayPriceByOrders(orderList);

        // 记录流量变动
        DataFlow dataFlow = new DataFlow(payRecord.getAccount(), FlowType.INFLOW, FlowSource.DATA_ORDER,
                totalPrice / 10, "用户微信支付商品价", new Date(), payRecord.getTradeId());
        userService.addDataFlow(dataFlow);

        // 记录福分变动
        BonusPointFlow pointFlow = new BonusPointFlow(payRecord.getAccount(), FlowType.INFLOW, FlowSource.POINT_ORDER,
                1, "用户微信支付商品价", new Date(), payRecord.getTradeId());
        userService.addPointFlow(pointFlow);
        // 更新订单成功
        updateOrderPaySuccess(orderList);
        if (StringUtils.isNotBlank(payRecord.getTradeId())) {
            shoppingCartDAO.deleteByTradeId(payRecord.getTradeId());
        }

        // 修改支付记录为成功
        payRecord.setResultDesc("支付成功");
        payRecord.setFinishTime(System.currentTimeMillis());
        payRecord.setPayResult(true);
        payRecordDao.update(PayRecord.class, payRecord);
    }

    @Override
    public void updateOrderPaySuccess(List<Order> orderList) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        for (Order order : orderList) {
            ProductPackage productPackage = productPackageDAO.find(ProductPackage.class, order.getPackageId());
            Integer remainedNum = productPackage.getRemainedNum() - order.getNumber();
            // 修改商品包库存
            productPackage.setRemainedNum(remainedNum);
            productPackage.setSellNum(productPackage.getSellNum() + order.getNumber());

            // 记录用户订单记录
            UserOrder userOrder = userOrderDAO.getUserOrderByOrderId(order.getId());
            if (userOrder != null) {
                userOrder.setLuckyCode(StringUtils.join(OrderUtil.geneLuckyCode(productPackage.getSellNum()-order.getNumber(),order.getNumber()),","));
                userOrderDAO.update(UserOrder.class, userOrder);
            }

            order.setProductPayDate(System.currentTimeMillis());
            order.setStatus(OrderStatus.ORDER_PAYED);
            order.setPackageStatus(productPackage.getPackageStatus());

            //判断如果这是该商品的最后一单，则先查询产品包中奖配置
            //如果是正常中奖，则不做处理，如果为指定中奖人或者内部用户中奖，则修改bookdate
            //
            if (productPackage.getRemainedNum() <= 0) {
                ProductPackageSetting pps = productPackageSettingService.getProductPackageSettingByPackageId(productPackage.getPackageId());
                productPackage.setPackageStatus(PackageStatus.SOLD_OUT);
                productPackage.setSoldOutTime(new Date());
                if (productPackage.getAutoGeneNextBatch()) {
                    //自动下一期，并且把产品包配置也延续
                    if(productPackage.getRemainedBatch()==null||productPackage.getRemainedBatch()>0){
                        Integer newRemainedBatch = null;
                        if(productPackage.getRemainedBatch()!=null){
                            newRemainedBatch = productPackage.getRemainedBatch()-1;
                        }
                        productPackage.setRemainedBatch(newRemainedBatch);
                        Integer newPackageId = productPackageDAO.geneNextBatchPackage(productPackage);
                        if(pps!=null){
                            ProductPackageSetting newPps = null;
                            Integer keepBatch = pps.getKeepBatch();
                            if(pps.getWinType()==3){
                                newPps = new ProductPackageSetting(newPackageId,1,0,0,null,null);
                            }else if(pps.getWinType()==2){
                                if(keepBatch!=null){
                                    if(keepBatch>0){
                                        newPps = new ProductPackageSetting(newPackageId,pps.getWinType(),pps.getAutoBuyMin(),pps.getAutoBuyMax(),pps.getAccount(),keepBatch-1);
                                    }else{
                                        newPps = new ProductPackageSetting(newPackageId,1,0,0,null,null);
                                    }
                                }else{
                                    newPps = new ProductPackageSetting(newPackageId,1,0,0,null,null);
                                }
                            }else if(pps.getWinType()==1){
                                newPps = new ProductPackageSetting(newPackageId,pps.getWinType(),pps.getAutoBuyMin(),pps.getAutoBuyMax(),pps.getAccount(),pps.getKeepBatch());
                            }
                            if(newPps!=null){
                                productPackageSettingService.save(newPps);
                            }
                        }
                    }
                }
                //修改本订单的下单时间，让内部用户或指定的用户中奖
                Long timeMillis = Long.valueOf(order.getDateNum());
                Integer minCs = getMinMinus(productPackage,pps);
                Long newDnDate = timeMillis-minCs;
                DecimalFormat df = new DecimalFormat("000000000");
                order.setDateNum(df.format(newDnDate));
                System.out.println("newDnDate:"+newDnDate);
                SimpleDateFormat ssdf = new SimpleDateFormat("yyyyMMdd");
                String newBookDateStr = ssdf.format(new Date(order.getBookDate()))+order.getDateNum();
                order.setBookDate(DateUtil.getDate(newBookDateStr,"yyyyMMddHHmmssSSS").getTime());
            }
            orderDAO.update(Order.class, order);
            productPackageDAO.update(ProductPackage.class, productPackage);

            // 如果用户有推荐用户，给推荐用户添加佣金
            User user = userService.getUserWithAccountId(userOrder.getAccount().getId());
            if (StringUtils.isNotBlank(user.getInviteId())) {
                User inviteUser = userService.getUserWithAccountId(user.getInviteId());
                if (inviteUser != null) {
                    CommissionFlow commissionFlow = new CommissionFlow(inviteUser.getAccount(), FlowType.INFLOW, FlowSource.COMMISSION_RECHARGE,
                            order.getPrice() * 0.06, "下级用户充值", new Date(), order.getId(), userOrder.getAccount(), order.getPrice());
                    userService.addCommissionFlow(commissionFlow, inviteUser);
                }
            }
            if (StringUtils.isNotBlank(user.getAgentId())) {
                User agentUser = userService.getUserWithAccountId(user.getAgentId());
                if (agentUser != null) {
                    CommissionFlow commissionFlow = new CommissionFlow(agentUser.getAccount(), FlowType.INFLOW, FlowSource.COMMISSION_RECHARGE,
                            order.getPrice() * 0.05, "下级用户充值", new Date(), order.getId(), userOrder.getAccount(), order.getPrice());
                    userService.addCommissionFlow(commissionFlow, agentUser);
                }
            }
        }
    }

    private Integer getMinMinus(ProductPackage productPackage,ProductPackageSetting pps){
        Integer minCs = 0;
        if(pps!=null){
            LinkedHashMap<String, String> orderBy = new LinkedHashMap<>();
            orderBy.put("bookDate", "desc");
            QueryResult<Order> queryResult = orderService.queryOrdersByPackageId(productPackage.getPackageId(), 0, 100,orderBy);
            String luckyCode = OrderUtil.genLuckyCode(productPackage.getTotalNum(),queryResult.getList());
            List<String> lcList = null;
            String querySql = null;
            if(pps.getWinType()==2){
                //先查询到所有内部用户的订单，并获取其中的所有LuckyCode
                querySql = "select uo.luckyCode from m_user_order uo left join t_account acc on uo.account_id = acc.requestId where uo.productPackage_packageId = "+productPackage.getPackageId()+" and acc.roleType = " + RoleType.BUILTIN.getIndex();
            }else if(pps.getWinType()==3){
                //先查询到指定用户的订单，并获取其中的所有LuckyCode
                querySql = "select uo.luckyCode from m_user_order uo where uo.productPackage_packageId = "+productPackage.getPackageId()+" and uo.account_id='"+pps.getAccount().getId()+"'";
            }
            if(StringUtils.isNotBlank(querySql)){
                lcList = userOrderDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(querySql).list();
                if(lcList!=null&&!lcList.isEmpty()){
                    Set<String> luckyCodeSet = new HashSet<>();
                    for (String lcz:lcList) {
                        if(StringUtils.isNotBlank(lcz)){
                            luckyCodeSet.addAll(Arrays.asList(lcz.split(",")));
                        }
                    }
                    //计算得到需要中奖的码和原幸运码差值最小的数
                    for (Iterator<String> iterator = luckyCodeSet.iterator(); iterator.hasNext();) {
                        String lc = iterator.next();
                        Integer minusNum = Integer.valueOf(luckyCode)-Integer.valueOf(lc);
                        minCs = minCs == 0?minusNum:Math.abs(minCs)>Math.abs(minusNum)?minusNum:minCs;
                    }
                }
            }
        }
        return minCs;
    }

    @Override
    public Map<String, Object> payOrderWithBalance(User user, List<Order> orderList, String orderId, String tradId) throws Exception {
        Map<String, Object> reMap = new HashMap<>();
        Double totalPrice = FormatOrder.getPayPriceByOrders(orderList);
        Integer totalNum = totalPrice!=null?Integer.valueOf(totalPrice.intValue()):0;
        // 在系统生成支付记录
        PayRecord payRecord = new PayRecord();
        payRecord.setAccount(user.getAccount());
        payRecord.setCreateTime(System.currentTimeMillis());
        payRecord.setPayFee(0.0);
        payRecord.setBalance(totalPrice);
        payRecord.setPayResult(true);
        payRecord.setFinishTime(System.currentTimeMillis());
        payRecord.setPayWay(PayWay.PAY_ONLINE_WITH_BALANCE);
        payRecord.setPayFor(PayFor.PACKAGE_ORDER);
        payRecord.setResultDesc("余额支付成功");
        payRecord.setTradeId(tradId);
        payRecord.setOrderId(orderId);
        payRecord.setTradeNo(null);
        payRecord.setCanceled(false);
        payRecordDao.add(PayRecord.class, payRecord);

        // 记录余额变动
        String relateId = tradId;
        if (StringUtils.isBlank(tradId)) {
            relateId = orderId;
        }
        BalanceFlow balanceFlow = new BalanceFlow(payRecord.getAccount(), FlowType.OUTFLOW, FlowSource.BALANCE_ORDER,
                totalPrice, "用户余额支付商品价", new Date(), relateId);
        userService.addBalanceFlow(balanceFlow);

        // 记录流量变动
        DataFlow dataFlow = new DataFlow(payRecord.getAccount(), FlowType.INFLOW, FlowSource.DATA_ORDER,
                totalPrice / 10, "用户余额支付商品价", new Date(), relateId);
        userService.addDataFlow(dataFlow);

        // 记录福分变动
        BonusPointFlow pointFlow = new BonusPointFlow(payRecord.getAccount(), FlowType.INFLOW, FlowSource.POINT_ORDER,
                totalNum, "用户余额支付商品价", new Date(), relateId);
        userService.addPointFlow(pointFlow);

        // 更新订单成功
        updateOrderPaySuccess(orderList);
        if (StringUtils.isNotBlank(tradId)) {
            shoppingCartDAO.deleteByTradeId(tradId);
        }

        reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        return reMap;
    }

    @Override
    public PayRecord getRecordByTradeNo(String trade_no) {
        String hql = "o.tradeNo=?";
        Object[] params = new Object[]{trade_no};
        List<PayRecord> list = payRecordDao.find(PayRecord.class, hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PayRecord getSuccessRecordByOrderId(String orderId, PayFor payFor) {
        String hql = "o.orderId=? and o.payFor=? and o.payResult=?";
        Object[] params = new Object[]{orderId, payFor, true};
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("o.finishTime", "desc");//根据接单时间 按降序排序
        List<PayRecord> list = payRecordDao.find(PayRecord.class, hql, params, order);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<PayRecord> queryByAccountId(String accountId) {
        String hql = "o.account.requestId=? and o.payResult=?";
        Object[] params = new Object[]{accountId, true};
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("o.finishTime", "desc");
        return payRecordDao.find(PayRecord.class, hql, params, order);
    }

    @Override
    public List<PayRecord> queryByOrderOrTradeId(String tradeId, String orderId) {
        String hql = "o.canceled=?";
        Object[] params;
        if (StringUtils.isNotBlank(tradeId)) {
            hql += " or o.tradeId=?";
            params = new Object[]{false, tradeId};
        } else {
            hql += " or o.orderId=?";
            params = new Object[]{false, orderId};
        }
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("o.createTime", "desc");
        return payRecordDao.find(PayRecord.class, hql, params, order);
    }

    @Override
    public void updateRecord(PayRecord payRecord) {
        payRecordDao.update(PayRecord.class, payRecord);
    }

    public static void main(String[] args){
        Long newDnDate = 5827668l;
        DecimalFormat df = new DecimalFormat("000000000");
        System.out.println(df.format(newDnDate));

    }
}
