package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.OrderCarePlan;
import lombok.Data;

import java.util.Date;

@Data
public class OrderCarePlanVo {

    private String id;

    private String planId;

    private String orderId;

    private String typeId;

    private String typeName;

    private String caseId;

    private String caseName;

    private String planDesc;

    private Date createTime;

    private String staffId;

    private String staffUserName;

    public OrderCarePlanVo(OrderCarePlan carePlan) {
        this.id = carePlan.getId();
        this.planId = carePlan.getPlanId();
        this.orderId = carePlan.getOrderId();
        this.typeId = carePlan.getTypeId();
        this.caseId = carePlan.getCaseId();
        this.planDesc = carePlan.getPlanDesc();
        this.createTime = carePlan.getCreateTime();
        this.staffId = carePlan.getStaffId();
    }

}
