package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.*;
import com.baymin.springboot.store.payload.BasicItemRequestVo;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String id;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "ORDER_USER_ID", length = 32)
    private String orderUserId;

    @ApiModelProperty(notes = "服务人员ID(护工)")
    @Column(name = "SERVICE_STAFF_ID", length = 32)
    private String serviceStaffId;

    @ApiModelProperty(notes = "服务人员ID(护士)")
    @Column(name = "NURSE_ID", length = 32)
    private String nurseId;

    @ApiModelProperty(notes = "服务监督人员ID", hidden = true)
    @Column(name = "SERVICE_ADMIN_ID", length = 32)
    private String serviceAdminId;

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
    @ApiModelProperty(notes = "订单来源 WECHAT/PC/WECHAT_QUICK")
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

    @ApiModelProperty(notes = "是否已评价")
    @Column(name = "EVALUATED")
    private Boolean evaluated;

    @ApiModelProperty(notes = "订单退款状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "REFUND_STATUS", length = 2)
    private CommonDealStatus refundStatus;

    @ApiModelProperty(notes = "是否全额退款")
    @Column(name = "FULL_REFUND")
    private Boolean fullRefund;

    @ApiModelProperty(notes = "护士/护工换人状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STAFF_CHANGE_STATUS", length = 2)
    private CommonDealStatus staffChangeStatus;

    @ApiModelProperty(notes = "是否已制定陪护计划")
    @Column(name = "CARE_PLAN_EXISTS")
    private Boolean carePlanExists;

    @ApiModelProperty(hidden = true)
    @Column(name = "OFFLINE_PAY_ADMIN_ID")
    private String offlinePayAdminId;

    @ApiModelProperty(notes = "订单备注")
    @Column(name = "REMARK", length = 2048)
    private String remark;

    @ApiModelProperty(notes = "医院")
    @Column(name = "HOSPITAL_NAME", length = 128)
    private String hospitalName;

    @ApiModelProperty(notes = "医院科室")
    @Column(name = "HOSPITAL_DEPARTMENT", length = 128)
    private String hospitalDepartment;

    @ApiModelProperty(notes = "床号")
    @Column(name = "BED_NO", length = 128)
    private String bedNo;

    @ApiModelProperty(notes = "服务范围")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_SCOPE", length = 2)
    private ServiceScope serviceScope;

    @ApiModelProperty(notes = "服务模式")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_MODE", length = 2)
    private ServiceMode serviceMode;

    /**
     * 乐观锁
     */
    @ApiModelProperty(hidden = true)
    @Version
    private int version;

    @ApiModelProperty(notes = "订单附加信息")
    @Transient
    private OrderExt orderExt;

    @ApiModelProperty(notes = "用户信息", hidden = true)
    @Transient
    private UserProfile userProfile;

    @ApiModelProperty(notes = "产品信息", hidden = true)
    @Transient
    private ServiceProduct product;

    @ApiModelProperty(hidden = true)
    @Transient
    private ServiceStaff serviceStaff;

    @ApiModelProperty(hidden = true)
    @Transient
    private ServiceStaff nurseStaff;

    @ApiModelProperty(hidden = true)
    @Transient
    private ServiceStaff adminStaff;

    @ApiModelProperty(hidden = true)
    @Transient
    private Admin admin;

    @ApiModelProperty(hidden = true)
    @Transient
    private Admin offlinePayAdmin;

    @ApiModelProperty(hidden = true)
    @Transient
    private double unitPrice;

}
