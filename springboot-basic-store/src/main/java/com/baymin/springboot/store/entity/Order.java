package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.payload.BasicItemRequestVo;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "订单基本信息表")
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_ORDER")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Order implements Serializable {

    @ApiModelProperty(notes = "订单ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "ORDER_USER_ID", length = 32)
    private String orderUserId;

    @ApiModelProperty(notes = "服务人员ID")
    @Column(name = "SERVICE_STAFF_ID", length = 32)
    private String serviceStaffId;

    @ApiModelProperty(notes = "产品ID")
    @Column(name = "serviceProductId", length = 32)
    private String serviceProductId;

    @ApiModelProperty(notes = "服务项目信息", hidden = true)
    @Type(type = "json")
    @Column(name = "BASIC_ITEM_INFO", columnDefinition = "json")
    private List<BasicItemRequestVo> basicItemInfo;

    @ApiModelProperty(notes = "订单时间")
    @Column(name = "ORDER_TIME", columnDefinition = "timestamp")
    private Date orderTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "CLOSE_TIME", columnDefinition = "timestamp")
    private Date closeTime;

    @ApiModelProperty(notes = "陪护类型")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    /**
     * 订单来源  WECHAT/PC
     */
    @ApiModelProperty(notes = "订单来源 WECHAT/PC")
    @Column(name = "ORDER_SOURCE", length = 10)
    private String orderSource;

    @ApiModelProperty(notes = "订单状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STATUS", length = 2)
    private OrderStatus status;

    @ApiModelProperty(notes = "订单总额")
    @Column(name = "TOTAL_FEE", precision = 10, scale = 2)
    private Double totalFee;

    /**
     * 支付方式
     */
    @ApiModelProperty(notes = "支付方式")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "PAY_WAY")
    private PayWay payWay;

    @ApiModelProperty(notes = "支付时间")
    @Column(name = "PAY_TIME", columnDefinition = "timestamp")
    private Date payTime;

    /**
     * 发票状态
     */
    @ApiModelProperty(notes = "开票状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "INVOICE_STATUS")
    private InvoiceStatus invoiceStatus;

    @ApiModelProperty(notes = "开票记录ID")
    @Column(name = "INVOICE_ID", length = 32)
    private String invoiceId;

    /**
     * 乐观锁
     */
    @ApiModelProperty(hidden = true)
    @Version
    private int version;

}
