package com.baymin.springboot.ut.hibernatevalidator.module;

import com.baymin.springboot.ut.hibernatevalidator.checkgroup.DriverChecks;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

/**
 * Created by jonez on 2017/3/22.
 */
public class Driver extends Person {

    @Min(value = 18, message = "You have to be 18 to drive a car", groups = DriverChecks.class)
    public int age;

    @AssertTrue(message = "You first have to pass the driving test", groups = DriverChecks.class)
    public boolean hasDrivingLicense;

    public Driver(String name) {
        super(name);
    }

    public void passedDrivingTest(boolean b) {
        hasDrivingLicense = b;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
