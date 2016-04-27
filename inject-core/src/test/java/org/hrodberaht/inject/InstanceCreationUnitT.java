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

package org.hrodberaht.inject;

import org.hrodberaht.inject.testservices.annotated.Car;
import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorCGLIB;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorDefault;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreationUnitT {


    @Test
    public void testConstructors() throws InterruptedException {

        Constructor constructor = getAnnotatedConstructor();

        Car aCar = createCar(constructor, new InstanceCreatorCGLIB());
        Car aCar2 = createCar(constructor, new InstanceCreatorDefault());

        assertEquals("volvo", aCar2.brand());
        assertEquals(aCar.brand(), aCar2.brand());
        assertNotNull(aCar.getSpareTire());
        assertNotNull(aCar2.getSpareTire());

    }

    private Car createCar(Constructor constructor, InstanceCreator instanceCreator) {

        Tire spareTire = new Tire();
        return (Volvo) instanceCreator.createInstance(constructor, spareTire);

    }

    public Constructor getAnnotatedConstructor() {
        for (Constructor constructor : Volvo.class.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        throw new IllegalAccessError("Bad call");
    }
}