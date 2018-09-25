package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "订单照护计划")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_CARE_PLAN")
public class OrderCarePlan {

    @ApiModelProperty(notes = "照护计划ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "系统照护计划ID")
    @Column(name = "PLAN_ID", length = 32)
    private String planId;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(notes = "类型ID")
    @Column(name = "TYPE_ID", length = 32)
    private String typeId;

    @ApiModelProperty(notes = "适应症ID")
    @Column(name = "CASE_ID", length = 32)
    private String caseId;

    @ApiModelProperty(notes = "照护详情")
    @Column(name = "PLAN_DESC", length = 2048)
    private String planDesc;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(notes = "护士/护工ID")
    @Column(name = "STAFF_ID", length = 32)
    private String staffId;

}
