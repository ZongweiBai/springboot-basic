package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "订单照护计划执行记录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_CARE_PLAN_RECORD")
public class OrderCarePlanRecord {

    @ApiModelProperty(notes = "执行记录ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(notes = "照护计划ID")
    @Column(name = "ORDER_PLAN_ID", length = 32)
    private String orderPlanId;

    @ApiModelProperty(notes = "照护计划子项ID")
    @Column(name = "ORDER_PLAN_SUB_ID", length = 32)
    private String orderPlanSubId;

    @ApiModelProperty(notes = "执行开始时间")
    @Column(name = "START_TIME", columnDefinition = "timestamp")
    private Date startTime;

    @ApiModelProperty(notes = "执行结束时间")
    @Column(name = "END_TIME", columnDefinition = "timestamp")
    private Date endTime;

    @ApiModelProperty(notes = "护工ID")
    @Column(name = "STAFF_ID", length = 32)
    private String staffId;

    @Transient
    @ApiModelProperty(notes = "护工信息")
    private String StaffName;

}
