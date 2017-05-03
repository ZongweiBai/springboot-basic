package com.baymin.springboot.testcase;

import com.baymin.springboot.SpringBootApp;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

/**
 * Created by Baymin on 2017/4/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootApp.class, webEnvironment = DEFINED_PORT)
public class UserProfileApiTest {

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = 8888;
        RestAssured.basePath = "/springboot/rest";
    }

    @Test
    public void testSayHelloSuccess() {
        given().contentType("application/json").expect().statusCode(200).when().get("/userProfile/12");
    }

    @Test
    public void testSayHelloFail() {
        given().contentType("application/json").expect().statusCode(404).when().get("/userProfile/16");
    }

}
