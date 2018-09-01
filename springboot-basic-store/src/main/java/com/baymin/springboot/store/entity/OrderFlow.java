package com.baymin.springboot.store.entity;


import com.baymin.springboot.store.enumconstant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单流程表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "T_ORDER_FLOW")
public class OrderFlow implements Serializable {

    private static final long serialVersionUID = -4824749589780482250L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "OLD_STATUS", length = 2)
    private OrderStatus oldStatus;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "NEW_STATUS", length = 2)
    private OrderStatus newStatus;

    @Column(name = "FLOW_DESC", length = 128)
    private String flowDesc;

    @Column(name = "FLOW_TIME", columnDefinition = "timestamp")
    private Long flowTime;

}
