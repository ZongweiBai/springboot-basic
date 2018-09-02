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
                .body(orderRequest).log().all()
                .post("/order/user/")
                .then().log().all().statusCode(200);
    }

}
