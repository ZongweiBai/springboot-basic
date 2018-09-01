package com.baymin.springboot.store.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 订单基本信息表
 */
@Data
@NoArgsConstructor
@Entity(name = "T_ORDER_EXT")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderExt implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Type(type = "json")
    @Column(name = "PATIENT_INFO", columnDefinition = "json")
    private Map<String, Object> patientInfo;

    @Column(name = "SERVICE_START_TIME", columnDefinition = "timestamp")
    private Long serviceStartTime;

    @Column(name = "SERVICE_END_TIME", columnDefinition = "timestamp")
    private Long serviceEndDate;

    @Column(name = "SERVICE_DURATION", precision = 10, scale = 2)
    private Double serviceDuration;

    @Column(name = "SERVICE_NUMBER", length = 4)
    private Integer serviceNumber;

    @Column(name = "SERVICE_ADDRESS", length = 128)
    private String serviceAddress;

    @Column(name = "CONTACT", length = 20)
    private String contact;

    @Column(name = "CONTACT_MOBILE", length = 20)
    private String contactMobile;

}
