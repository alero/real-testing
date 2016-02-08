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

package test.org.hrodberaht.inject;

import junit.framework.TestCase;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.hrodberaht.inject.Container;


/**
 * Simple Java Utils - Injection
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:27:43
 * @version 1.0
 * @since 1.0
 */

public class TestSuiteJsr330Tck extends TestCase {

    public static junit.framework.Test suite() {

        Container container = TckUtil.prepareRegister().getContainer();

        final Car car = container.get(Car.class);
        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;

        return Tck.testsFor(car, supportsStatic, supportsPrivate);
    }
}
