package com.baymin.springboot.store.enumconstant;

/**
 * 支付方式
 */
public enum PayWay {

    PAY_ONLINE_WITH_WECHAT("微信支付", 0),
    PAY_OFFLINE_CASH("线下支付-现金", 1),
    PAY_OFFLINE_POS("线下支付-POS", 2),
    PAY_OFFLINE_ALI("线下支付-支付宝", 3),
    PAY_OFFLINE_WECHAT("线下支付-微信", 4);

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
