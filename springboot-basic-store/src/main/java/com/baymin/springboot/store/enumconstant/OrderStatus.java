package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * Created by Administrator on 2015/1/4 0004.
 */
public enum OrderStatus implements IBaseDbEnum {

    ORDER_UN_PAY("已下单待付款", 0),
    ORDER_PAYED("已付款待指派", 1),
    ORDER_ASSIGN("已指派待服务", 2),
    ORDER_PROCESSING("服务中", 3),
    ORDER_FINISH("已完成", 4),
    ORDER_FULL_REFUND("已全额退款", -98),
    ORDER_CANCELED("已取消", -99);

    OrderStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    private String name;
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
