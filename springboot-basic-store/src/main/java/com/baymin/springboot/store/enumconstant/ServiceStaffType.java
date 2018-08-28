package com.baymin.springboot.store.enumconstant;

import lombok.Getter;

@Getter
public enum ServiceStaffType {

    NURSE("护士", 0),
    WORKERS("护工", 1);


    private String name;
    private int index;

    ServiceStaffType(String name, int index) {
        this.name = name;
        this.index = index;
    }

}
