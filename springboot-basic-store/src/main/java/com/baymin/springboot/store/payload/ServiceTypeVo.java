package com.baymin.springboot.store.payload;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.CareType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@ApiModel(description = "服务类别")
@Data
@NoArgsConstructor
public class ServiceTypeVo {

    @ApiModelProperty(notes = "类别ID")
    private String id;

    @ApiModelProperty(notes = "服务类别名称")
    private String serviceName;

    @ApiModelProperty(notes = "服务类别图标")
    private String serviceIcon;

    @ApiModelProperty(notes = "服务类别枚举")
    private CareType careType;

    @ApiModelProperty(notes = "服务类别描述")
    private String serviceDesc;

    public ServiceTypeVo(ServiceType serviceType) {
        if (Objects.isNull(serviceType)) {
            return;
        }
        this.id = serviceType.getId();
        this.serviceName = serviceType.getServiceName();
        this.serviceIcon = Constant.IMG_HOST + serviceType.getServiceIcon();
        this.careType = serviceType.getCareType();
        this.serviceDesc = serviceType.getServiceDesc();
    }
}
