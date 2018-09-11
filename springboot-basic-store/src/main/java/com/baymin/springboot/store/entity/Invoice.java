package com.baymin.springboot.store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name = "TAX_NO", length = 32)
    private String taxNo;

    @Column(name = "INVOICE_FEE", precision=10, scale=2)
    private Double invoiceFee;

    @Column(name = "DEAL_STATUS")
    private Boolean dealStatus;

    /**
     * 创建日期
     */
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    /**
     * 处理日期
     */
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date DEAL_Time;

    @Transient
    private String orderIds;
}
