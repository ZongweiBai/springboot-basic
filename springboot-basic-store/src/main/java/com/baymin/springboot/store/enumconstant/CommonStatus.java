package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * 共用状态类型
 */
public enum CommonStatus implements IBaseDbEnum {

    NORMAL("正常", 1),
    FORBIDDEN("禁用", 0),
    UPCART("上架", 10),
    DOWNCART("下架", 11),
    DELETE("已删除", -99);

    private String name;
    private int index;

    CommonStatus(String name, int index) {
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
