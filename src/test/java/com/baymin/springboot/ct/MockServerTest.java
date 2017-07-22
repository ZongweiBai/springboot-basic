//package com.baymin.springboot.ct;
//
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.junit.Assert;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockserver.client.server.MockServerClient;
//import org.mockserver.junit.MockServerRule;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.mockserver.model.HttpRequest.request;
//import static org.mockserver.model.HttpResponse.response;
//
///**
// * Created by Baymin on 2017/5/17.
// */
//public class MockServerTest {
//
//    @Rule
//    public MockServerRule server = new MockServerRule(this, 28097);
//
//    @Test
//    public void testMockServer() throws IOException {
//        MockServerClient mockClient = new MockServerClient("127.0.0.1", 28097);
//        String expected = "{ message: 'incorrect username and password combination' }";
//        mockClient.when(
//                request()
//                        .withPath("/hello/John")
//                        .withMethod("GET")
//        ).respond(
//                response()
//                        .withStatusCode(200)
//                        .withBody(expected)
//        );
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("http://127.0.0.1:28097/hello/John");
//        CloseableHttpResponse response = client.execute(httpGet);
//        //验证输出是否是正确
//        InputStream content = response.getEntity().getContent();
//        InputStreamReader inputStreamReader = new InputStreamReader(content);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String responseText = bufferedReader.readLine();
//        Assert.assertThat(responseText, equalTo(expected));
//
//        mockClient.stop();
//
//        CloseableHttpClient client1 = HttpClients.createDefault();
//        HttpGet httpGet1 = new HttpGet("http://127.0.0.1:28097/hello/John");
//        CloseableHttpResponse response1 = client1.execute(httpGet);
//        //验证输出是否是正确
//        InputStream content1 = response1.getEntity().getContent();
//        InputStreamReader inputStreamReader1 = new InputStreamReader(content);
//        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
//        String responseText1 = bufferedReader1.readLine();
//        Assert.assertThat(responseText1, equalTo(expected));
//
//    }
//
//}
