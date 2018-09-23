package com.baymin.springboot.store.payload.request;

import lombok.Data;

@Data
public class LoginRequest {

    String account;
    String smsCode;
    String password;

}
