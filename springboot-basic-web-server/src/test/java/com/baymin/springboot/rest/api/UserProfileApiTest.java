package com.baymin.springboot.rest.api;

import com.baymin.springboot.store.entity.UserProfile;
import io.restassured.RestAssured;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Baymin on 2017/4/22.
 */
public class UserProfileApiTest extends AbstractTest {

    @Test
    public void testQuerySuccess() {
        String account = "account" + new Random().nextInt(10000);

        UserProfile userProfile = new UserProfile();
        userProfile.setNickName("nickname" + new Random().nextInt(10000));
        userProfile.setActualName("actualName" + new Random().nextInt(10000));
        userProfile.setPassword("123456");
        userProfile.setAccount(account);
        RestAssured.given().contentType("application/json").body(userProfile).post("/userProfile/").then().statusCode(204);

        RestAssured.given().contentType("application/json")
                .pathParam("account", account).get("/userProfile/{account}").then().statusCode(200);
    }

    @Test
    public void testQueryFail() {
        RestAssured.given().contentType("application/json")
                .pathParam("account", "16").get("/userProfile/{account}").then().statusCode(404);
    }

}
