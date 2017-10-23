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

package org.hrodberaht.injection.core.register;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.core.register.internal.RegistrationExtended;
import org.hrodberaht.injection.core.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jun-17 19:11:31
 * @version 1.0
 * @since 1.0
 */
public abstract class RegistrationModuleAnnotation implements RegistrationModule<RegistrationInstanceSimple> {

    private InjectionFinder injectionFinderImplementation;
    private InstanceCreator instanceCreatorImplementation;
    protected Map<RegistrationInstanceSimple, RegistrationInstanceSimple>
            registrations = new HashMap<RegistrationInstanceSimple, RegistrationInstanceSimple>();

    protected RegistrationModuleAnnotation() {
        registrations();
    }

    protected void registerInjectionFinder(InjectionFinder injectionFinder) {
        this.injectionFinderImplementation = injectionFinder;
    }

    protected void registerInstanceCreator(InstanceCreator instanceCreator) {
        this.instanceCreatorImplementation = instanceCreator;
    }

    public RegistrationExtended register(Class serviceClass) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(serviceClass);
        registrations.put(instance, instance);
        return instance;
    }

    public RegistrationExtended register(Class serviceClass, Class interfaceClass) {
        RegistrationInstanceSimple instance = new RegistrationInstanceSimple(interfaceClass).with(serviceClass);
        registrations.put(instance, instance);
        return instance;
    }

    public InjectionFinder getInjectionFinder() {
        return injectionFinderImplementation;
    }

    public InstanceCreator getInstanceCreator() {
        return instanceCreatorImplementation;
    }

    @Override
    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registrations.values();
    }

    @Override
    public List<RegistrationInstanceSimple> getRegistrationsList() {
        return new ArrayList<>(registrations.values());
    }

    public void putRegistrations(List<RegistrationInstanceSimple> registrationList) {
        for (RegistrationInstanceSimple registrationInstanceSimple : registrationList) {
            registrations.put(registrationInstanceSimple, registrationInstanceSimple);
        }
    }

    public abstract void registrations();

    @Override
    public void postRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }

    @Override
    public void preRegistration(InjectContainer injectContainer) {
        // Not a forced method
    }
}
