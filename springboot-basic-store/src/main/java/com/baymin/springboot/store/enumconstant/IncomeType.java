package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 收入类型
 */
public enum IncomeType implements IBaseDbEnum {

    INCOME("收入", 1),
    WITHDRAW("提现", 2);

    private String name;
    private int index;

    IncomeType(String name, int index) {
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
