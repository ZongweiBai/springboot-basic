package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.OrderType;
import com.baymin.springboot.store.enumconstant.PayWay;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单基本信息表
 */
@Data
@NoArgsConstructor
@Entity(name = "T_ORDER")
public class Order implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_USER_ID", length = 32)
    private String orderUserId;

    @Column(name = "SERVICE_STAFF_ID", length = 32)
    private String serviceStaffId;

    @Column(name = "ORDER_TIME", columnDefinition = "timestamp")
    private Long orderTime;

    @Column(name = "CLOSE_TIME", columnDefinition = "timestamp")
    private Long closeTime;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "ORDER_TYPE")
    private OrderType orderType;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STATUS", length = 2)
    private OrderStatus status;

    @Column(name = "TOTAL_FEE", precision=10, scale=2)
    private Double totalFee;

    /**
     * 支付方式
     */
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "PAY_WAY")
    private PayWay payWay;

    /**
     * 发票状态
     */
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "INVOICE_STATUS")
    private InvoiceStatus invoiceStatus;

    /**
     * 乐观锁
     */
    @Version
    private int version;

}
