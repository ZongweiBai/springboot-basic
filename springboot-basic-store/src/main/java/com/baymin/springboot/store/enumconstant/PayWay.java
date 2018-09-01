package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 支付方式
 */
public enum PayWay implements IBaseDbEnum {

    PAY_ONLINE_WITH_WECHAT("微信支付", 10),
    PAY_OFFLINE_CASH("线下支付-现金", 20),
    PAY_OFFLINE_POS("线下支付-POS", 21),
    PAY_OFFLINE_ALI("线下支付-支付宝", 22),
    PAY_OFFLINE_WECHAT("线下支付-微信", 24);

    private String name;
    private int index;

    PayWay(String name, int index) {
        this.name = name;
        this.index = index;
    }

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
