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

package org.hrodberaht.inject.guice;

import junit.framework.TestCase;


/**
 * Simple Java Utils - Injection
 *
 * @author Robert Alexandersson
 * 2010-maj-28 19:27:43
 * @version 1.0
 * @since 1.0
 */

public class GuiceSuiteJsr330TckUnitT extends TestCase {

    public static junit.framework.Test suite() {

        /*InjectionRegisterJava registerJava = new InjectionRegisterJava()
                .activateContainerGuice();

        // Guice does not work with the TCK at version 2.0, need to wait for new release.
        registerJava.registerGuiceModule(new GuiceTckModule());
        InjectContainer container = registerJava.getContainer();



        final Car car = container.getInnerContainer(Car.class);

        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;
        return Tck.testsFor(car, supportsStatic, supportsPrivate);
        //
*/
        return null;
    }
}