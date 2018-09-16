package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 开票信息
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_INVOICE")
public class Invoice {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 发票类型 E：电子  P：纸质
     */
    @Column(name = "INVOICE_TYPE", length = 2)
    private String invoiceType;

    /**
     * 抬头类型 C:企业单位  P:个人
     */
    @Column(name = "HEADER_TYPE", length = 2)
    private String headerType;

    @Column(name = "INVOICE_HEADER", length = 128)
    private String invoiceHeader;

    @Column(name = "TAX_NO", length = 32)
    private String taxNo;

    @Column(name = "INVOICE_FEE", precision=10, scale=2)
    private Double invoiceFee;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    /**
     * 创建日期
     */
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    /**
     * 处理日期
     */
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    @Column(name = "RECIPIENT", length = 32)
    private String recipient;

    @Column(name = "RECIPIENT_MOBILE", length = 32)
    private String recipientMobile;

    @Column(name = "RECIPIENT_ADDRESS", length = 32)
    private String recipientAddress;

    @Column(name = "ORDER_IDS", length = 1024)
    private String orderIds;
}
