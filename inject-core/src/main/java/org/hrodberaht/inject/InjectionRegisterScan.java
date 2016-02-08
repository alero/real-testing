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

import org.hrodberaht.inject.register.InjectionRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScan extends InjectionRegisterBase<InjectionRegisterScan> {


    private ClassScanner classScanner = new ClassScanner();

    public InjectionRegisterScan() {
    }

    public InjectionRegisterScan(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterScan registerBasePackageScan(String packagename) {
        Class[] clazzs = classScanner.getClasses(packagename);
        for (Class aClazz : clazzs) {
            classScanner.createRegistration(aClazz, container);
        }
        return this;
    }

    public InjectionRegisterScan registerBasePackageScan(String packagename, Class... manuallyexcluded) {
        Class[] clazzs = classScanner.getClasses(packagename);
        List<Class> listOfClasses = new ArrayList<Class>(clazzs.length);

        // remove the manual excludes
        for (Class aClazz : clazzs) {
            if (!manuallyExcluded(aClazz, manuallyexcluded)) {
                listOfClasses.add(aClazz);
            }
        }
        for (Class aClazz : listOfClasses) {
            classScanner.createRegistration(aClazz, container);
        }
        return this;
    }

    public void setDetailedScanLogging(boolean detailedScanLogging) {
        this.classScanner.setDetailedScanLogging(detailedScanLogging);
    }

    @Override
    public InjectionRegisterScan clone() {
        InjectionRegisterScan registerScan = new InjectionRegisterScan();
        try {
            registerScan.classScanner.getCustomClassLoaders().addAll(this.classScanner.getCustomClassLoaders());
            registerScan.classScanner.setDetailedScanLogging(this.classScanner.isDetailedScanLogging());
            registerScan.container = this.container.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return registerScan;
    }


    private boolean manuallyExcluded(Class aClazz, Class[] manuallyexluded) {
        for (Class excluded : manuallyexluded) {
            if (excluded == aClazz) {
                return true;
            }
        }
        return false;
    }


}