package com.baymin.springboot.store.payload;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.ServiceProduct;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class ServiceProductVo {

    private String productId;

    private String productName;

    private String productIcon;

    private String serviceTypeId;

    private Double productPrice;

    private Date createTime;

    private String serviceTips;

    private String appointmentNotice;

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
        if (Objects.isNull(itemMap) || itemMap.isEmpty()) {
            this.itemMap = itemMap;
        }
    }

}
