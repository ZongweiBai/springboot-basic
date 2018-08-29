package com.baymin.springboot.store.enumconstant;

/**
 * Created by Administrator on 2015/1/4 0004.
 */
public enum InvoiceStatus {

    NOT_INVOICED("未开票", 0),
    INVOICING("开票中", 1),
    INVOICED("已开票", 2);

    InvoiceStatus(String name, int index) {
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
