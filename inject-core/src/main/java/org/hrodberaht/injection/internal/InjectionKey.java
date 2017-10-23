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

package org.hrodberaht.injection.internal;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 * <p/>
 * Immutable, must stay this way as hashcode relies on this
 *
 * @author Robert Alexandersson
 * 2010-jun-03 19:57:23
 * @version 1.0
 * @since 1.0
 */
public class InjectionKey {

    private Class<? extends Annotation> annotation;
    private String name;
    private Class serviceDefinition;
    // is the service a javax.injectMethod.Provider
    private boolean provider = false;
    private int hashCode;

    public InjectionKey(String name, Class serviceDefinition, boolean provider) {
        this.name = name;
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
        createHashCode();
    }

    public InjectionKey(Class<? extends Annotation> annotation, Class serviceDefinition, boolean provider) {
        this.annotation = annotation;
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
        createHashCode();
    }

    public InjectionKey(Class serviceDefinition, boolean provider) {
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
        createHashCode();
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public String getName() {
        return name;
    }

    public Class getServiceDefinition() {
        return serviceDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InjectionKey that = (InjectionKey) o;

        if (this.provider != that.provider) {
            return false;
        }

        if (annotation != null ? !annotation.equals(that.annotation) : that.annotation != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (serviceDefinition != null ?
                !serviceDefinition.equals(that.serviceDefinition) : that.serviceDefinition != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private void createHashCode() {
        int result = annotation != null ? annotation.hashCode() : 0;
        result = 31 * result + (provider ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (serviceDefinition != null ? serviceDefinition.hashCode() : 0);
        hashCode = result;
    }

    public String getQualifier() {
        if (name != null) {
            return name;
        }
        if (annotation != null) {
            return annotation.getName();
        }
        return null;
    }

    public boolean isProvider() {
        return provider;
    }

    /**
     * Makes a clone that is not marked as a Provider for implementation search
     *
     * @param key
     * @return
     */
    public static InjectionKey purify(InjectionKey key) {
        InjectionKey injectionKey = new InjectionKey(key.getServiceDefinition(), false);
        injectionKey.annotation = key.annotation;
        injectionKey.name = key.name;
        return injectionKey;
    }
}
