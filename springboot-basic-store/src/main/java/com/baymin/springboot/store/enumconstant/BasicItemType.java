package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 基础收费项目类型
 */
public enum BasicItemType implements IBaseDbEnum {

    BASIC("基础护理", 1),
    MEDICAL("医疗照护", 2),
    LIFE("活动照护", 3),
    HOME("生活服务", 4);

    private String name;
    private int index;

    BasicItemType(String name, int index) {
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
