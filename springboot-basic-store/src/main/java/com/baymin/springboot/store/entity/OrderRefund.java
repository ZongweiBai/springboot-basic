package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_REFUND")
public class OrderRefund {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @Column(name = "REFUND_FEE", precision = 10, scale = 0)
    private Double refundFee;

    @Column(name = "REFUND_DURATION", precision = 10, scale = 2)
    private Double refundDuration;

    @Column(name = "REFUND_TIME", columnDefinition = "timestamp")
    private Date refundTime;

    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    @Column(name = "REFUND_DESC", length = 1024)
    private String refundDesc;

    @Column(name = "DEAL_DESC", length = 1024)
    private String dealDesc;

    /********************bank info************************/
    @Column(name = "BANK_ACCOUNT_USER_NAME", length = 20)
    private String bankAccountUserName;

    @Column(name = "BANK_ACCOUNT_NUMBER", length = 32)
    private String bankAccountNumber;

    @Column(name = "BANK_NAME", length = 64)
    private String bankName;

}
