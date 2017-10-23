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

import org.hrodberaht.injection.core.register.internal.RegistrationInstanceSimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-03 17:49:42
 * @version 1.0
 * @since 1.0
 */
public abstract class ExtendedModule extends RegistrationModuleAnnotation {
    protected RegistrationModuleAnnotation registration = null;

    public Collection<RegistrationInstanceSimple> getRegistrations() {
        return registration.getRegistrations();
    }

    @Override
    public List<RegistrationInstanceSimple> getRegistrationsList() {
        return new ArrayList<>(getRegistrations());
    }

    @Override
    public void registrations() {
        // just do nothing
    }

}
