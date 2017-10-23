/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.inject.testservices.annotated.VolvoManufacturer;
import org.hrodberaht.inject.testservices.annotated_extra.Manufacturer;
import org.hrodberaht.inject.testservices.annotated_extra.Saab;
import org.hrodberaht.inject.testservices.annotated_extra.SaabManufacturer;
import org.hrodberaht.injection.core.register.VariableInjectionFactory;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-26 21:53:03
 * @version 1.0
 * @since 1.0
 */
public class ManufacturerVariableFactory implements VariableInjectionFactory<Car, Manufacturer> {
    public Class<? extends Car> getInstanceClass(Manufacturer variable) {
        if (variable instanceof SaabManufacturer) {
            return Saab.class;
        }
        if (variable instanceof VolvoManufacturer) {
            return Volvo.class;
        }
        return null;
    }

    public Class getInstanceType() {
        return Car.class;
    }
}
