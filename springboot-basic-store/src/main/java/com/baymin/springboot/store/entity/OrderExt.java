package com.baymin.springboot.store.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@ApiModel(description = "订单附加信息表")
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_ORDER_EXT")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderExt implements Serializable {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(hidden = true)
    @Type(type = "json")
    @Column(name = "PATIENT_INFO", columnDefinition = "json")
    private Map<String, Object> patientInfo;

    @ApiModelProperty(notes = "服务开始时间")
    @Column(name = "SERVICE_START_TIME", columnDefinition = "timestamp")
    private Date serviceStartTime;

    @ApiModelProperty(notes = "服务结束时间")
    @Column(name = "SERVICE_END_TIME", columnDefinition = "timestamp")
    private Date serviceEndDate;

    @ApiModelProperty(notes = "服务时长")
    @Column(name = "SERVICE_DURATION", precision = 10, scale = 2)
    private Double serviceDuration;

    @ApiModelProperty(notes = "服务次数")
    @Column(name = "SERVICE_NUMBER", length = 4)
    private Integer serviceNumber;

    @ApiModelProperty(notes = "服务地址")
    @Column(name = "SERVICE_ADDRESS", length = 128)
    private String serviceAddress;

    @ApiModelProperty(notes = "联系人")
    @Column(name = "CONTACT", length = 20)
    private String contact;

    @ApiModelProperty(notes = "联系人手机")
    @Column(name = "CONTACT_MOBILE", length = 20)
    private String contactMobile;

}
