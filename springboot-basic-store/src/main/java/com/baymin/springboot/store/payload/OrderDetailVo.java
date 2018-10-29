package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.entity.ServiceStaff;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "订单详情")
@Data
public class OrderDetailVo {

    @ApiModelProperty(notes = "订单基本信息")
    private Order order;

    @ApiModelProperty(notes = "附加信息")
    private OrderExt orderExt;

    @ApiModelProperty(notes = "订单服务产品信息")
    private ServiceProductVo serviceProduct;

    @ApiModelProperty(notes = "护士/护工详情")
    private ServiceStaff serviceStaff;

}
