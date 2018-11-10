package com.baymin.springboot.pay.wechat.param.jsApi;

import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.pay.wechat.param.Signature;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
@Data
@ApiModel(description = "微信JS支付所需信息")
public class JsApiOrderReqData {

	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String packageInfo;
	private String signType = "MD5";
	private String paySign;

	@ApiModelProperty(notes = "扫码支付二维码图片URL")
	private String nativePayQrcode;
	
	public JsApiOrderReqData() {
		super();
	}
	
	public JsApiOrderReqData(UnifiedOrderResData resData, String key) {
		if (StringUtils.equals(resData.getTrade_type(), "JSAPI")) {
			setAppId(resData.getAppid());
			setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
			setPackageInfo("prepay_id=" + resData.getPrepay_id());
			setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
			setSignType("MD5");
			String sign = Signature.getSign(toMap(), key);
			setPaySign(sign);
		} else {
			setNativePayQrcode(resData.getCode_url());
		}
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
