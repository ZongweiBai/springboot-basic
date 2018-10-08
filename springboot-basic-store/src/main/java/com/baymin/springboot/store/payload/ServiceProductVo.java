package com.baymin.springboot.store.payload;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.ServiceProduct;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApiModel(description = "推荐方案")
@Data
public class ServiceProductVo {

    @ApiModelProperty(notes = "方案ID")
    private String productId;

    @ApiModelProperty(notes = "方案名称")
    private String productName;

    @ApiModelProperty(notes = "方案图标")
    private String productIcon;

    @ApiModelProperty(notes = "对应服务类别ID")
    private String serviceTypeId;

    @ApiModelProperty(notes = "方案价格")
    private Double productPrice;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(notes = "服务提示")
    private String serviceTips;

    @ApiModelProperty(notes = "预约须知")
    private String appointmentNotice;

    @ApiModelProperty(notes = "推荐方案详细基本服务项，只用于居家和医院陪护")
    private Map<BasicItemType, List<BasicItem>> itemMap;

    public ServiceProductVo(ServiceProduct product, Map<BasicItemType, List<BasicItem>> itemMap) {
        if (Objects.isNull(product)) {
            return;
        }
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.productIcon = Constant.IMG_HOST + product.getProductIcon();
        this.serviceTypeId = product.getServiceTypeId();
        this.createTime = product.getCreateTime();
        this.serviceTips = product.getServiceTips();
        this.appointmentNotice = product.getAppointmentNotice();
        if (Objects.nonNull(itemMap) && !itemMap.isEmpty()) {
            this.itemMap = itemMap;
        }
    }

}
