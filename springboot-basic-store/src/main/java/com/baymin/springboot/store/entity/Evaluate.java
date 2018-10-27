package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "订单评价")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_EVALUATE")
public class Evaluate implements Serializable {

    private static final long serialVersionUID = 3109228357668258013L;

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(notes = "陪护类型", hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @ApiModelProperty(notes = "评分等级 1-5星")
    @Column(name = "GRADE")
    private Integer grade = 5;

    @ApiModelProperty(notes = "评价详情")
    @Column(name = "DESCRIPTION", length = 2048)
    private String description;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ApiModelProperty(hidden = true, notes = "审核状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus auditStatus;

    @ApiModelProperty(hidden = true, notes = "审核时间")
    @Column(name = "AUDIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditTime;

    @ApiModelProperty(hidden = true, notes = "审核备注")
    @Column(name = "AUDIT_DESC", length = 1024)
    private String auditDesc;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "USER_ID", length = 32)
    private String userId;

    @ApiModelProperty(hidden = true)
    @Transient
    private UserProfile userProfile;

}
