package com.baymin.springboot.webserver.api;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Baymin on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = port;
        RestAssured.basePath = "/family-care";
    }

}
