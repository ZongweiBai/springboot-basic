package com.baymin.springboot.hibernatevalidator.test;

import com.baymin.springboot.hibernatevalidator.module.Car;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by jonez on 2017/3/22.
 */
public class CarTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void manufacturerIsNull() {
//        Car car = new Car(null, "DD-AB-123", 4);
        Car car = new Car(null, null, 4);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(2, constraintViolations.size());
        assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
        for (ConstraintViolation<Car> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
            System.out.println(constraintViolation.getMessageTemplate());
            System.out.println(constraintViolation.getRootBean());
            System.out.println(constraintViolation.getRootBeanClass());
            System.out.println(constraintViolation.getLeafBean());
            System.out.println(constraintViolation.getPropertyPath());
            System.out.println(constraintViolation.getInvalidValue());
            System.out.println(constraintViolation.getConstraintDescriptor());
            System.out.println("************************************");
        }
    }

    @Test
    public void licensePlateTooShort() {
        Car car = new Car("Morris", "D", 4);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 2 and 14", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void seatCountTooLow() {
        Car car = new Car("Morris", "DD-AB-123", 1);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(1, constraintViolations.size());
        assertEquals("must be greater than or equal to 2", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void carIsValid() {
        Car car = new Car("Morris", "DD-AB-123", null);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(0, constraintViolations.size());
    }

}
