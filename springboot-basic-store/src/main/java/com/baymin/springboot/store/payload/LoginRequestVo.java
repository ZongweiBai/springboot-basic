package com.baymin.springboot.store.payload;

import lombok.Data;

@Data
public class LoginRequestVo {

    String account;
    String smsCode;
    String password;

}
