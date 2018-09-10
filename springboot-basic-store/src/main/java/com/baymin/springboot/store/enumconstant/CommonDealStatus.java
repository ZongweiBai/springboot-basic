package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 共用状态类型
 */
public enum CommonDealStatus implements IBaseDbEnum {

    APPLY("已申请", 0),
    AGREE("已同意", 1),
    REJECT("已驳回", -1);

    private String name;
    private int index;

    CommonDealStatus(String name, int index) {
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
