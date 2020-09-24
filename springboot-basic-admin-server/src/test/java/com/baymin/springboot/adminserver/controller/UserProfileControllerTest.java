package com.baymin.springboot.adminserver.controller;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UserProfileControllerTest {

    @Test
    void testViewUserDetail() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("userName", "root");
        loginBody.put("passWord", "root");
        String sessionId = RestAssured.given().contentType(ContentType.JSON)
                .queryParams(loginBody)
                .log().all()
                .post("http://localhost:9999/admin/system/login")
                .then().log().all()
                .statusCode(200).extract().cookie("JSESSIONID");

        RestAssured.given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .queryParam("userId", "2c92b642748cbb0c0174b8ea6b3200d1")
                .log().all()
                .get("http://localhost:9999/admin/user/viewUserDetail")
                .then().log().all()
                .statusCode(200);
    }
}