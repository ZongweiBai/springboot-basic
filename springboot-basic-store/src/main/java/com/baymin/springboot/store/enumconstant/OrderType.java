package com.baymin.springboot.store.enumconstant;

/**
 * 陪护类型
 */
public enum OrderType {

    HOSPITAL_CARE("医院陪护", 0),
    HOME_CARE("居家照护", 1),
    REHABILITATION("康复护理", 2);

    private String name;
    private int index;

    OrderType(String name, int index) {
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
