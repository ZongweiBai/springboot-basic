package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 服务模式类型
 */
public enum ServiceMode implements IBaseDbEnum {

    ONE_TO_ONE("一对一", 0),
    ONE_TO_MANY("一对多", 1),
    MANY_TO_ONE("多对一", 1);

    private String name;
    private int index;

    ServiceMode(String name, int index) {
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
