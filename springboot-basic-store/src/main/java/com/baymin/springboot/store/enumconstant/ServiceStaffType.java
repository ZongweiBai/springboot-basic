package com.baymin.springboot.store.enumconstant;

import com.baymin.springboot.store.enumconstant.convert.IBaseDbEnum;
import lombok.Getter;

@Getter
public enum ServiceStaffType implements IBaseDbEnum {

    NURSE("护士", 0),
    WORKER("护工", 1),
    SUPERVISOR("督导", 2);


    private String name;
    private int index;

    ServiceStaffType(String name, int index) {
        this.name = name;
        this.index = index;
    }

}
