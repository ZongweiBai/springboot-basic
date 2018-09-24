package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import lombok.Data;

@Data
public class OrderDetailVo {

    private Order order;

    private OrderExt orderExt;

    private ServiceProductVo serviceProduct;

}
