package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;

/**
 * Created by Administrator on 2015/1/4 0004.
 */
public enum ServiceStatus implements IBaseDbEnum {

    FREE("空闲", 0),
    ASSIGNED("已指派", 1),
    IN_SERVICE("服务中", 2);

    ServiceStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    private String name;
    private int index;

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
