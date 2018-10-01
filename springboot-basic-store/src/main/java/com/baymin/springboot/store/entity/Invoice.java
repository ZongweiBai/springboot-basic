package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "开票信息")
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_INVOICE")
public class Invoice {

    @ApiModelProperty(hidden = false)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "发票类型 E：电子  P：纸质")
    @Column(name = "INVOICE_TYPE", length = 2)
    private String invoiceType;

    @ApiModelProperty(notes = "抬头类型 C:企业单位  P:个人")
    @Column(name = "HEADER_TYPE", length = 2)
    private String headerType;

    @ApiModelProperty(notes = "发票抬头")
    @Column(name = "INVOICE_HEADER", length = 128)
    private String invoiceHeader;

    @ApiModelProperty(notes = "税号")
    @Column(name = "TAX_NO", length = 32)
    private String taxNo;

    @ApiModelProperty(notes = "开票金额")
    @Column(name = "INVOICE_FEE", precision = 10, scale = 2)
    private Double invoiceFee;

    @ApiModelProperty(hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    /**
     * 创建日期
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    /**
     * 处理日期
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    @ApiModelProperty(notes = "纸质发票收货人")
    @Column(name = "RECIPIENT", length = 32)
    private String recipient;

    @ApiModelProperty(notes = "纸质发票收货人手机")
    @Column(name = "RECIPIENT_MOBILE", length = 32)
    private String recipientMobile;

    @ApiModelProperty(notes = "纸质发票收货人地址")
    @Column(name = "RECIPIENT_ADDRESS", length = 32)
    private String recipientAddress;

    @ApiModelProperty(notes = "订单ID，多个订单以,分隔")
    @Column(name = "ORDER_IDS", length = 1024)
    private String orderIds;
}
