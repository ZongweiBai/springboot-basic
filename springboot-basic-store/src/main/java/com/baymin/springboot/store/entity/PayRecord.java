package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.PayWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_PAY_RECORD")
public class PayRecord implements Serializable {

    private static final long serialVersionUID = 8755986028863452969L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 支付人
     */
    @Column(name = "PAY_USER_ID", length = 32)
    private String payerUserId;

    /**
     * 支付金额
     */
    @Column(name = "PAY_FEE")
    private Double payFee;

    /**
     * 支付类型
     */
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "PAY_WAY")
    private PayWay payWay;

    /**
     * 支付结果
     */
    @Column(name = "PAY_RESULT")
    private Boolean payResult;
    /**
     * 支付结果说明
     */
    @Column(name = "RESULT_DESC", length = 128)
    private String resultDesc;
    /**
     * 创建日期
     */
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;
    /**
     * 支付完成日期
     */
    @Column(name = "FINISH_TIME", columnDefinition = "timestamp")
    private Date finishTime;
    /**
     * 订单ID
     */
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;
    /**
     * 支付订单号
     */
    @Column(name = "TRADE_NO", length = 32)
    private String tradeNo;
    /**
     * 在线支付订单号(交易订单号)
     */
    @Column(name = "TRANSACTION_ID", length = 32)
    private String transactionId;

}
