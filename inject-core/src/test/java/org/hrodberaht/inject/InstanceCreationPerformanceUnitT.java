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

import org.hrodberaht.inject.testservices.annotated.Tire;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorCGLIB;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorDefault;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
@Category({PerformanceTests.class})
public class InstanceCreationPerformanceUnitT {

    @Test(timeout = 10000)
    public void testPerformance() throws InterruptedException, NoSuchMethodException {

        // warm-up
        runPerformanceTest(new InstanceCreatorCGLIB(), 10);

        int iterations = 300000;

        System.gc();
        Thread.sleep(100L);
        System.out.println("TestRun with CGLIB - " +
                runPerformanceTest(new InstanceCreatorCGLIB(), iterations) +
                "ms with " + iterations + " iterations, meaning " + iterations * 2 + " objects created");

        System.gc();
        Thread.sleep(100L);
        System.out.println("TestRun with java Reflection - " +
                runPerformanceTest(new InstanceCreatorDefault(), iterations) +
                "ms with " + iterations + " iterations, meaning " + iterations * 2 + " objects created");

        System.gc();
        Thread.sleep(100L);
        System.out.println("TestRun with pure java - " +
                runPerformanceTest(null, iterations) +
                "ms with " + iterations + " iterations, meaning " + iterations * 2 + " objects created");

    }

    private Constructor<?> getVolvoConstructor() throws NoSuchMethodException {
        return Volvo.class.getConstructor(Tire.class);
    }

    private Constructor<?> getTireConstructor() throws NoSuchMethodException {
        return Tire.class.getConstructor();
    }

    private long runPerformanceTest(InstanceCreator instanceCreator, int iterations) throws NoSuchMethodException {
        long startTime = new Date().getTime();
        Volvo[] volvos = new Volvo[iterations];
        Constructor constructorVolvo = getVolvoConstructor();
        Constructor constructorTire = getTireConstructor();
        for (int i = 0; i < iterations; i++) {

            if (instanceCreator != null) {
                dynamicContructor(instanceCreator, volvos, constructorVolvo, constructorTire, i);
            } else {
                classicConstructor(volvos, i);
            }
        }
        long endTime = new Date().getTime();
        return endTime - startTime;
    }

    private void classicConstructor(Volvo[] volvos, int i) {
        Tire spareTire = new Tire();
        Volvo aVolvo = new Volvo(spareTire);
        volvos[i] = aVolvo;
    }

    private void dynamicContructor(InstanceCreator instanceCreator, Volvo[] volvos,
                                   Constructor constructorVolvo, Constructor constructorTire, int i) {
        Tire spareTire = (Tire) instanceCreator.createInstance(constructorTire);
        Volvo aVolvo = (Volvo) instanceCreator.createInstance(constructorVolvo, spareTire);
        volvos[i] = aVolvo;
    }
}