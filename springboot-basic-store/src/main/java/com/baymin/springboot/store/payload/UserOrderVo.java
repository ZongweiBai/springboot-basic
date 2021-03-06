package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.ServiceMode;
import com.baymin.springboot.store.enumconstant.ServiceScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;

@ApiModel(description = "用户下单数据")
@Data
public class UserOrderVo {

    @ApiModelProperty(notes = "用户ID")
    private String orderUserId;

    @ApiModelProperty(notes = "陪护类型")
    private CareType orderType;

    @ApiModelProperty(notes = "订单总额")
    private Double totalFee;

    @ApiModelProperty(notes = "支付方式 在线：ONLINE_WECHAT  线下：OFFLINE")
    private String payway; //支付方式 ONLINE_WECHAT  OFFLINE

    @ApiModelProperty(notes = "开票信息")
    private Invoice invoice;

    @ApiModelProperty(notes = "开始服务日期")
    private Long serviceStartDate;

    @ApiModelProperty(notes = "结束服务日期")
    private Long serviceEndDate;

    @ApiModelProperty(notes = "服务时长（居家或者陪护）")
    private Double serviceDuration;

    @ApiModelProperty(notes = "服务次数（康复护理）")
    private Integer serviceNumber;

    @ApiModelProperty(notes = "联系人姓名")
    private String contact;

    @ApiModelProperty(notes = "联系人手机号码")
    private String contactMobile;

    @ApiModelProperty(notes = "服务地址")
    private String serviceAddress;

    @ApiModelProperty(notes = "医院地址")
    private String hospitalAddress;

    @ApiModelProperty(notes = "产品ID（方案ID）")
    private String productId;

    @ApiModelProperty(notes = "基础项目列表")
    private List<BasicItemRequestVo> basicItems;

    @ApiModelProperty(notes = "问题列表")
    private List<Question> questions;

    @ApiModelProperty(notes = "订单来源 WECHAT：前端 PC：代下单 WECHAT_QUICK：微信快捷开单")
    private String orderSource;

    @ApiModelProperty(notes = "订单备注")
    private String remark;

    @ApiModelProperty(notes = "医院名称")
    private String hospitalName;

    @ApiModelProperty(notes = "医院科室")
    private String hospitalDepartment;

    @ApiModelProperty(notes = "床号")
    private String bedNo;

    @ApiModelProperty(notes = "服务范围")
    private ServiceScope serviceScope;

    @ApiModelProperty(notes = "服务模式")
    private ServiceMode serviceMode;

    @ApiModelProperty(notes = "服务监督人员ID")
    private String serviceAdminId;

}
