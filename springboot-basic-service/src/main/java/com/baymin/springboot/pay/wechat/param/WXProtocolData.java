package com.baymin.springboot.pay.wechat.param;

import lombok.Data;

@Data
public class WXProtocolData {

    // 通知参数
    private String return_code; // 返回状态码，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断；SUCCESS/FAIL
    private String return_msg; // 返回信息，如非空，为错误原因

}