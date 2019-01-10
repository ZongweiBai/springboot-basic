package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 服务范围类型
 */
public enum ServiceScope implements IBaseDbEnum {

    INSIDE("院内", 0),
    OUTSIDE("院外", 1);

    private String name;
    private int index;

    ServiceScope(String name, int index) {
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
