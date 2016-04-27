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

package org.hrodberaht.inject.annotation;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.hrodberaht.inject.PerformanceTests;
import org.hrodberaht.inject.TckUtil;
import org.hrodberaht.inject.testservices.annotated.Volvo;
import org.hrodberaht.inject.util.PerformanceStatistics;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.internal.stats.Statistics;
import org.hrodberaht.injection.register.InjectionRegister;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
@Category({PerformanceTests.class})
public class AnnotationContinerPerformanceUnitT {

    private InjectionRegister registerVolvo;
    final int threadCount = 100;
    final int threadIterations = 1000;

    @Before
    public void init() {
        registerVolvo = AnnotationContainerUtil.prepareLargeVolvoRegister();
        // Statistics.setEnabled(true);
    }

    @After
    public void destroy() {

        Statistics.setEnabled(false);
    }

    @Test(timeout = 100000)
    public void testPerformance() {
        InjectContainer container = TckUtil.prepareRegister().getContainer();
        PerformanceStatistics performanceStatistics = new PerformanceStatistics("testPerformance");
        for (int i = 0; i < threadIterations; i++) {
            // This does loads of fetching from the container, will stress test it a lot.
            // Form what i could see on the Cobertura report each rotation give about 100 calls.
            // meaning these 1 000 iterations will test about 100 000 calls to the SimpleInjection.getInnerContainer method.

            // On my machine an Intel i7 820 this takes about 2 seconds using 1 of 4 CPU's at 75%.
            // This is not strange as this test is not threaded in any way.
            runningOfContainerTCK(container, performanceStatistics);
        }
        performanceStatistics.end();
        printContainerStatistics(performanceStatistics);
    }

    @Test(timeout = 10000)
    public void testMultiThreadPerformance() {


        multiThreadedTest();
    }

    private void multiThreadedTest() {
        final InjectContainer container = registerVolvo.getContainer();
        Collection<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(
                    new Thread() {
                        @Override
                        public void run() {
                            runThreadContainerGet(container);
                        }
                    }
            );
        }
        PerformanceStatistics performanceStatistics = new PerformanceStatistics("testMultiThreadPerformance");
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            waitForIt(thread);
        }
        performanceStatistics.end();
        printContainerStatistics(performanceStatistics);
    }

    private void printContainerStatistics(PerformanceStatistics performanceStatistics) {
        System.out.println(performanceStatistics.getName() +
                " Time in Milliseconds:" + ChronoUnit.MILLIS.between(performanceStatistics.getStart(), performanceStatistics.getEnd())
        );
        System.out.println(
                MessageFormat.format("Created objects: {0}", Statistics.getNewInstanceCount())
        );
        System.out.println(
                MessageFormat.format("Injected field count: {0}", Statistics.getInjectFieldCount())
        );
        System.out.println(
                MessageFormat.format("Injected method count: {0}", Statistics.getInjectMethodCount())
        );
        System.out.println(
                MessageFormat.format("Injected construct count: {0}", Statistics.getInjectConstructorCount())
        );
    }

    private void waitForIt(Thread thread) {
        try {
            while (thread.isAlive())
                Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void runningOfContainerTCK(InjectContainer container, PerformanceStatistics performanceStatistics) {
       //  performanceStatistics.markAndPrint("before get");
        Car car = container.get(Car.class);
        // performanceStatistics.markAndPrint("after get");
        Tck.testsFor(car, false, true);
        // performanceStatistics.markAndPrint("after testsFor");
    }

    private void runThreadContainerGet(InjectContainer container) {
        for (int i = 0; i < threadIterations; i++) {
            Volvo car = container.get(Volvo.class);
            AnnotationContainerUtil.assertVolvo(car);
        }
    }

}
