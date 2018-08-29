package com.baymin.springboot.store.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单基本信息表
 */
@Data
@NoArgsConstructor
@Entity(name = "t_order")
@TypeDef(
        name = "jsonb-node",
        typeClass = JsonNodeBinaryType.class
)
public class OrderExt implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Type(type = "jsonb-node")
    @Column(name = "PATIENT_INFO", columnDefinition = "jsonb")
    private JsonNode patientInfo;

    @Column(name = "SERVICE_START_TIME", columnDefinition = "timestamp")
    private Date serviceStartTime;

    @Column(name = "SERVICE_END_TIME", columnDefinition = "timestamp")
    private Date serviceEndDate;

    @Column(name = "SERVICE_DURATION", length = 10)
    private Double serviceDuration;

    @Column(name = "SERVICE_NUMBER", length = 4)
    private Integer serviceNumber;

    @Column(name = "SERVICE_AddRESS", length = 128)
    private String serviceAddress;

    @Column(name = "CONTACT", length = 20)
    private String contact;

    @Column(name = "CONTACT_MOBILE", length = 20)
    private String contactMobile;

}
