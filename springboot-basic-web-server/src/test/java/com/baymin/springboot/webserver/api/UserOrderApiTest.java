package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.payload.UserOrderRequest;
import io.restassured.RestAssured;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserOrderApiTest extends AbstractTest {

    @Test
    public void saveUserOrder() {
        UserOrderRequest orderRequest = new UserOrderRequest();
        orderRequest.setContact("libai");
        orderRequest.setContactMobile("18666177436");

        Map<String, Object> patientInfo = new HashMap<>();
        patientInfo.put(RequestConstant.DISEASES, "高血压, 骨折");
        patientInfo.put(RequestConstant.SELF_CARE, "全护理");
        patientInfo.put(RequestConstant.EATING, "鼻伺喂食");
        patientInfo.put(RequestConstant.ASSIST_WITH_MEDICATION, "输液看护, 胰岛素注射");
        patientInfo.put(RequestConstant.CATHETER_CARE, "伤口引流管, 负压引流管");
        orderRequest.setExtension(patientInfo);

        Invoice invoice = new Invoice();
        invoice.setHeaderType("C");
        invoice.setTaxNo("1235456");
        invoice.setInvoiceType("E");
        orderRequest.setInvoice(invoice);

        orderRequest.setOrderType(CareType.HOME_CARE);
        orderRequest.setPayway("ONLINE_WECHAT");
        orderRequest.setServiceAddress("南方医院1111号");
        orderRequest.setServiceDuration(2.5);
        orderRequest.setServiceStartDate(System.currentTimeMillis());
        orderRequest.setServiceEndDate(System.currentTimeMillis());

        RestAssured.given().contentType("application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJJX0FNX1JPQk9UIiwiaWF0IjoxNTM2ODUwMDc2LCJzdWIiOiJ7XCJ0b2tlblR5cGVcIjpcImFjY2Vzc190b2tlblwiLFwidXNlcklkXCI6XCJmZjgwODE4MTY1YmQ2MWY4MDE2NWJkNjJhM2Q5MDAwMFwifSIsImV4cCI6MTUzNjg1MzY3Nn0.YSOMwsGsCC4AqhlhkxFnwJzgqN-cCPGy0R0tCLQ1eofKJ7SJVEJhqYg_W4DWpTPwYddRlQ41TGUa0phORYOEfA")
                .body(orderRequest).log().all()
                .post("/api/order/")
                .then().log().all().statusCode(200);
    }

}
