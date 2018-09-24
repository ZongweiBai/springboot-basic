package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_CARE_PLAN")
public class OrderCarePlan {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "PLAN_ID", length = 32)
    private String planId;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Column(name = "TYPE_ID", length = 32)
    private String typeId;

    @Column(name = "CASE_ID", length = 32)
    private String caseId;

    @Column(name = "PLAN_DESC", length = 2048)
    private String planDesc;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "STAFF_ID", length = 32)
    private String staffId;

}
