package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 陪护类型
 */
public enum CareType implements IBaseDbEnum {

    HOSPITAL_CARE("医院陪护", 1),
    HOME_CARE("居家照护", 2),
    REHABILITATION("康复护理", 3);

    private String name;
    private int index;

    CareType(String name, int index) {
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
