package com.baymin.springboot.pay.wechat.param.unifiedorder;

import com.baymin.springboot.pay.wechat.WechatConfig;
import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.pay.wechat.param.Signature;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
public class UnifiedOrderReqData {

	private String appid; // 必填	公众账号ID
	private String mch_id; // 必填	商户号，微信支付分配的商户号
	private String device_info; // 非必填	 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	private String nonce_str; // 必填	随机字符串，不长于32位
	private String sign; // 必填	签名
	private String body; // 必填	商品描述
	private String detail; // 非必填	商品详情
	private String attach; // 非必填	说明，附加数据
	private String out_trade_no; // 必填	商户系统内部的订单号
	private String fee_type; // 非必填	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private int total_fee; // 必填	订单总金额，只能为整数
	private String spbill_create_ip; // 必填	终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	private String time_start; // 非必填	交易起始时间，订单生成时间，格式为yyyyMMddHHmmss
	private String time_expire; // 非必填	交易结束时间，格式为yyyyMMddHHmmss
	private String goods_tag; // 非必填	商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
	private String notify_url; // 必填	通知地址，接收微信支付异步通知回调地址
	private String trade_type; // 必填	交易类型，取值如下：JSAPI 公众号支付，NATIVE 扫码支付，APP，WAP
	private String product_id; // 非必填	商品ID，当trade_type=NATIVE（即扫码支付），此参数必传。此id为二维码中包含的商品ID，商户自行定义
	private String limit_pay; // 非必填	指定支付方式，no_credit--指定不能使用信用卡支付
	private String openid; // 非必填	当trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
	
	public UnifiedOrderReqData() {
		super();
	}
	
	public UnifiedOrderReqData(String deviceInfo, String tradeType,
							   String body, int totalFree, String appID, String mchID,
							   String key, String channelCode, String openId, String productId) {
		setAppid(appID);
		System.out.println("appID:"+appID);
		setMch_id(mchID);
		System.out.println("mchID:"+mchID);
		setDevice_info(deviceInfo);
		System.out.println("deviceInfo:"+deviceInfo);
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		System.out.println("nonce_str:"+getNonce_str());
		setBody(body);
		System.out.println("body:"+body);
		setOut_trade_no(RandomStringGenerator.getTradeNo(channelCode));
		System.out.println("out_trade_no:"+getOut_trade_no());
		setTotal_fee(totalFree);
		System.out.println("totalFree:"+totalFree);
		setSpbill_create_ip(WechatConfig.ip);
		System.out.println("spbill_create_ip:"+getSpbill_create_ip());
		setNotify_url(WechatConfig.notifyUrl);
		System.out.println("notify_url:"+getNotify_url());
		setTrade_type(tradeType);
		System.out.println("Trade_type:"+getTrade_type());
		setOpenid(openId);
		System.out.println("openId:"+openId);
		setProduct_id(productId);
		System.out.println("productId:"+productId);
		String sign = Signature.getSign(toMap(), key);
		setSign(sign);
		System.out.println("sign:"+sign);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}
	
}