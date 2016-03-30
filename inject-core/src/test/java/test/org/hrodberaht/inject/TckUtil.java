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

import org.atinject.tck.auto.*;
import org.atinject.tck.auto.accessories.SpareTire;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.InjectionRegister;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:51
 * @version 1.0
 * @since 1.0
 */
public class TckUtil {

    public static InjectionRegister prepareRegister() {
        InjectionRegisterModule registerJava = new InjectionRegisterModule();

        RegistrationModuleAnnotation module = new RegistrationModuleAnnotation() {

            public void registrations() {
                register(Car.class).with(Convertible.class);
                register(Engine.class).with(V8Engine.class);
                register(Tire.class).named("spare").with(SpareTire.class);
                register(Seat.class).annotated(Drivers.class).with(DriversSeat.class);
            }
        };
        registerJava.register(module);
        return registerJava;
    }


}
