package com.baymin.springboot.ct;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

/**
 * Created by Baymin on 2017/4/22.
 */
public class UserProfileApiTest extends AbstractTest {

    @Test
    public void testSayHelloSuccess() {
        RestAssured.given().contentType("application/json")
                .pathParam("account", "12").get("/userProfile/{account}").then().statusCode(200);
    }

    @Test
    public void testSayHelloFail() {
        RestAssured.given().contentType("application/json")
                .pathParam("account", "16").get("/userProfile/{account}").then().statusCode(404);
    }

}
