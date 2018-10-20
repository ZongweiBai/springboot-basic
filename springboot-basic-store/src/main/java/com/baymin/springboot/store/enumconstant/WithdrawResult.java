package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

public enum WithdrawResult implements IBaseDbEnum {
    UN_DEAL("处理中", 0),
    SUCCESS("已到帐", 1),
    DENY("已拒绝", 2);
    private String name;
    private int index;

    WithdrawResult(String name, int index) {
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
