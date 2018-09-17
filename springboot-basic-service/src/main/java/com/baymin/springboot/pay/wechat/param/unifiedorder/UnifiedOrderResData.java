package com.baymin.springboot.pay.wechat.param.unifiedorder;

import lombok.Data;

@Data
public class UnifiedOrderResData {

	// 返回状态码 SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	private String return_code = "";
	// 返回信息 如非空，为错误原因。如：签名失败、参数格式校验错误
	private String return_msg = "";

	// 协议返回的具体数据（以下字段在return_code为SUCCESS 的时候有返回）
	private String appid = "";
	private String mch_id = "";
	private String device_info = "";
	private String nonce_str = "";
	private String sign = ""; // 微信返回的签名
	private String result_code = ""; // 业务结果 SUCCESS/FAIL
	private String err_code = ""; // 错误代码
	private String err_code_des = ""; // 错误代码描述

	// 以下字段在return_code和result_code都为SUCCESS的时候有返回
	private String trade_type = ""; // 交易类型
	private String prepay_id = ""; // 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	
	// 以下字段返回给客户端使用
	private String out_trade_no;
	// trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	private String code_url;

}