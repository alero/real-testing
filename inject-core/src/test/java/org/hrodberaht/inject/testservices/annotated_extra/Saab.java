package org.hrodberaht.inject.testservices.annotated_extra;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Spare;
import org.hrodberaht.inject.testservices.annotated.TestDriver;
import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.inject.testservices.annotated.VindShield;

import javax.inject.Inject;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-26 21:36:54
 * @version 1.0
 * @since 1.0
 */
public class Saab implements Car {


    @Inject
    @Spare
    Tire spareTire;

    @Inject
    @Spare
    VindShield spareVindShield;

    @Inject
    TestDriver testDriver;

    public String brand() {
        return "Saab";
    }

    public Tire getSpareTire() {
        return spareTire;
    }

    public VindShield getSpareVindShield() {
        return spareVindShield;
    }

    public TestDriver getDriver() {
        return testDriver;
    }
}
