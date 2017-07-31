package com.baymin.springboot.rest.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.jayway.restassured.RestAssured;

/**
 * Created by Baymin on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = port;
        RestAssured.basePath = "/springboot/rest";
    }

}
