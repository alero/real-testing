/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject.testservices.annotated;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-maj-29 18:00:28
 * @version 1.0
 * @since 1.0
 */
public class Volvo implements Car {

    @Inject
    @Spare
    Tire spareTire;

    @Inject
    @Spare
    VindShield spareVindShield;

    @Inject
    Tire frontLeft;
    @Inject
    Tire frontRight;
    @Inject
    Tire backRight;
    @Inject
    Tire backLeft;

    @Inject
    VindShield vindShield;

    @Inject
    TestDriver driver;

    @Inject
    TestDriverManager driverManager;

    private String information = null;

    private String initText = null;
    private String initTextSpecial = null;

    @Injected
    Tire specialInjectField;
    Tire specialInjectMethod;

    @SpecialResource
    String someInformation;

    @Inject
    public Volvo(@Spare Tire spareTire) {
        this.spareTire = spareTire;
    }

    @Injected
    public void setSpecialInjectMethod(Tire specialInjectMethod) {
        this.specialInjectMethod = specialInjectMethod;
    }

    public Volvo() {

    }

    @PostConstruct
    public void init() {
        if (someInformation != null) {
            initText = "Initialized " + someInformation;
        } else {
            initText = "Initialized";
        }

    }

    public String brand() {
        return "volvo";
    }

    public TestDriver getDriver() {
        return driver;
    }

    public TestDriverManager getDriverManager() {
        return driverManager;
    }

    public Tire getSpareTire() {
        return spareTire;
    }

    public VindShield getSpareVindShield() {
        return spareVindShield;
    }

    public Tire getFrontLeft() {
        return frontLeft;
    }

    public Tire getFrontRight() {
        return frontRight;
    }

    public Tire getBackRight() {
        return backRight;
    }

    public Tire getBackLeft() {
        return backLeft;
    }

    public VindShield getVindShield() {
        return vindShield;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInitText() {
        return initText;
    }

    public String getInitTextSpecial() {
        return initTextSpecial;
    }

    public Tire getSpecialInjectField() {
        return specialInjectField;
    }

    public Tire getSpecialInjectMethod() {
        return specialInjectMethod;
    }
}
