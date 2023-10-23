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

package org.hrodberaht.injection.annotation;

import org.hrodberaht.injection.core.internal.InjectionRegisterScan;
import org.hrodberaht.injection.testservices.annotated_scan.JakartaSingleton;
import org.hrodberaht.injection.testservices.annotated_scan.JavaxSingleton;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-okt-18 20:45:45
 * @version 1.0
 * @since 1.0
 */
public class ScanContainerRegistrationUnitT {

    @Test
    public void testJavaxSingletonService() {
        InjectionRegisterScan registerJava = new InjectionRegisterScan();
        registerJava.scanPackage("org.hrodberaht.injection.testservices.annotated_scan");

        JavaxSingleton singleton1 = registerJava.getInjectContainer().get(JavaxSingleton.class);
        JavaxSingleton singleton2 = registerJava.getInjectContainer().get(JavaxSingleton.class);

        assertTrue(singleton1 == singleton2);

    }

    @Test
    public void testJakartaSingletonService() {
        InjectionRegisterScan registerJava = new InjectionRegisterScan();
        registerJava.scanPackage("org.hrodberaht.injection.testservices.annotated_scan");

        JakartaSingleton singleton1 = registerJava.getInjectContainer().get(JakartaSingleton.class);
        JakartaSingleton singleton2 = registerJava.getInjectContainer().get(JakartaSingleton.class);

        assertTrue(singleton1 == singleton2);

    }




}
