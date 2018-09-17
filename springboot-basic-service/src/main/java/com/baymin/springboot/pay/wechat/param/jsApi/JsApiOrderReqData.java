package com.baymin.springboot.pay.wechat.param.jsApi;

import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.pay.wechat.param.Signature;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信H5支付Request
 * @Title: JsApiOrderReqData.java
 * @ClassName: JsApiOrderReqData
 * @author: baizongwei
 * @date: 2016年1月21日 下午5:04:55
 * @version V1.0
 */
public class JsApiOrderReqData {

	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String packageInfo;
	private String signType = "MD5";
	private String paySign;
	
	public JsApiOrderReqData() {
		super();
	}
	
	public JsApiOrderReqData(UnifiedOrderResData resData, String key) {
		setAppId(resData.getAppid());
		setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
		setPackageInfo("prepay_id="+resData.getPrepay_id());
		setTimeStamp(String.valueOf(System.currentTimeMillis()/1000));
		setSignType("MD5");
		String sign = Signature.getSign(toMap(), key);
		setPaySign(sign);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
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
