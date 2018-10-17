package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.OrderCarePlan;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderCarePlanVo {

    private String id;

    private String orderId;

    private String typeId;

    private String typeName;

    private String caseId;

    private String caseName;

    private List<SubCarePlanVo> subCarePlans;

    private Date createTime;

    private String staffId;

    private String staffUserName;

    public OrderCarePlanVo(OrderCarePlan carePlan) {
        this.id = carePlan.getId();
        this.orderId = carePlan.getOrderId();
        this.typeId = carePlan.getTypeId();
        this.caseId = carePlan.getCaseId();
        this.subCarePlans = carePlan.getSubCarePlans();
        this.createTime = carePlan.getCreateTime();
        this.staffId = carePlan.getStaffId();
    }

}
