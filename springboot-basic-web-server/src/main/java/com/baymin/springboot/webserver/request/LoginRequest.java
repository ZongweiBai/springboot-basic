package com.baymin.springboot.webserver.request;

import lombok.Data;

@Data
public class LoginRequest {

    String account;
    String smsCode;
    String password;

}
