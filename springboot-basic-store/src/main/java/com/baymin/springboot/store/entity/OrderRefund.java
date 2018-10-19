package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "退款申请")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_REFUND")
public class OrderRefund {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "USER_ID", length = 32)
    private String userId;

    @ApiModelProperty(notes = "服务类型")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @ApiModelProperty(notes = "退款金额")
    @Column(name = "REFUND_FEE", precision = 10, scale = 0)
    private Double refundFee;

    @ApiModelProperty(notes = "退款时间段（开始）")
    @Column(name = "BEGIN_REFUND_PERIOD", length = 32)
    private String beginRefundPeriod;

    @ApiModelProperty(notes = "退款时间段（结束）")
    @Column(name = "END_REFUND_PERIOD", length = 32)
    private String endRefundPeriod;

    @ApiModelProperty(notes = "退款的次数（时长）")
    @Column(name = "REFUND_DURATION", precision = 10, scale = 2)
    private Double refundDuration;

    @ApiModelProperty(hidden = true, notes = "创建时间")
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(hidden = true, notes = "财务退款时间")
    @Column(name = "REFUND_TIME", columnDefinition = "timestamp")
    private Date refundTime;

    @ApiModelProperty(hidden = true, notes = "审核时间")
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    @ApiModelProperty(notes = "申请处理状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    @ApiModelProperty(notes = "退款原因")
    @Column(name = "REFUND_DESC", length = 1024)
    private String refundDesc;

    @ApiModelProperty(hidden = true)
    @Column(name = "DEAL_DESC", length = 1024)
    private String dealDesc;

    /********************bank info************************/
    @ApiModelProperty(notes = "银行账户")
    @Column(name = "BANK_ACCOUNT_USER_NAME", length = 20)
    private String bankAccountUserName;

    @ApiModelProperty(notes = "银行账号")
    @Column(name = "BANK_ACCOUNT_NUMBER", length = 32)
    private String bankAccountNumber;

    @ApiModelProperty(notes = "开户行名称")
    @Column(name = "BANK_NAME", length = 64)
    private String bankName;

    /********************联系人信息************************/
    @ApiModelProperty(notes = "退款联系人")
    @Column(name = "REFUND_CONTACT", length = 50)
    private String refundContact;

    @ApiModelProperty(notes = "联系方式")
    @Column(name = "CONTACT_MOBILE", length = 32)
    private String contactMobile;

}
