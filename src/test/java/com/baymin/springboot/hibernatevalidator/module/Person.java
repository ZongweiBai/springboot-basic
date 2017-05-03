package com.baymin.springboot.hibernatevalidator.module;

import javax.validation.constraints.NotNull;

/**
 * Created by jonez on 2017/3/22.
 */
public class Person {

    @NotNull(message = "may not be null")
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
