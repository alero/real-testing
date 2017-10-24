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

package org.hrodberaht.injection.core.internal.annotation;

import org.hrodberaht.injection.core.internal.InjectionContainerManager;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.core.internal.annotation.scope.ObjectAndScope;
import org.hrodberaht.injection.core.internal.annotation.scope.ScopeHandler;
import org.hrodberaht.injection.core.internal.annotation.scope.VariableScopeHandler;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class InjectionMetaData {

    private InjectionKey key;
    private Class serviceClass;
    private boolean preDefined = false;
    private Boolean accessible = null;
    private ScopeHandler scopeHandler;

    private Constructor constructor;
    private List<InjectionMetaData> constructorDependencies;
    private List<InjectionPoint> injectionPoints = new ArrayList<>();
    private Method postConstruct;
    private InstanceCreator instanceCreator;

    public InjectionMetaData(Class serviceClass, InjectionKey key, InstanceCreator instanceCreator) {
        this.serviceClass = serviceClass;
        this.key = key;
        this.instanceCreator = instanceCreator;
    }

    public InjectionKey getKey() {
        return key;
    }


    void setScopeHandler(ScopeHandler scopeHandler) {
        this.scopeHandler = scopeHandler;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    List<InjectionMetaData> getConstructorDependencies() {
        return constructorDependencies;
    }

    void setConstructorDependencies(List<InjectionMetaData> constructorDependencies) {
        this.constructorDependencies = constructorDependencies;
    }

    void setInjectionPoints(List<InjectionPoint> injectionPoints) {
        this.injectionPoints = injectionPoints;
    }

    public List<InjectionPoint> getInjectionPoints() {
        return injectionPoints;
    }

    boolean isPreDefined() {
        return preDefined;
    }

    void setPreDefined(boolean preDefined) {
        this.preDefined = preDefined;
    }

    public Class getServiceClass() {
        return this.serviceClass;
    }

    public InjectionContainerManager.Scope getScope() {
        return this.scopeHandler.getScope();
    }

    void setPostConstructMethod(Method postConstruct) {
        this.postConstruct = postConstruct;
    }

    Method getPostConstruct() {
        return this.postConstruct;
    }

    Class createVariableInstance(Object variable) {
        Class scopedInstanceClass = ((VariableScopeHandler) scopeHandler).getInstanceClass(variable);
        if (scopedInstanceClass != null) {
            return scopedInstanceClass;
        }
        throw new InjectRuntimeException("createVariableInstance failed for class {0} variable {1}"
                , serviceClass, variable);

    }

    ObjectAndScope createNewInstance(Object[] parameters) {
        verifyAccess();

        Object newInstance = instanceCreator.createInstance(constructor, parameters);
        scopeHandler.addInstance(newInstance);
        return new ObjectAndScope(newInstance, true);

    }

    public ObjectAndScope createInstance(Object[] parameters) {
        Object scopedInstance = scopeHandler.getInstance();
        if (scopedInstance != null) {
            if (scopeHandler.isInstanceCreated()) {
                return new ObjectAndScope(scopedInstance, true);
            }
            {
                return new ObjectAndScope(scopedInstance, false);
            }
        }
        verifyAccess();

        Object newInstance = instanceCreator.createInstance(constructor, parameters);
        scopeHandler.addInstance(newInstance);
        return new ObjectAndScope(newInstance, true);

    }

    private void verifyAccess() {
        if (accessible == null) {
            final boolean originalAccessible = constructor != null && constructor.isAccessible();
            if (!originalAccessible && constructor != null) {
                constructor.setAccessible(true);
            }
            accessible = true;
        }
    }


    boolean canInject(final InjectionMetaData bean) {
        if (bean == null) {
            return false;
        }

        if (bean == this) {
            return true;
        }

        if (bean.key.isProvider() != this.key.isProvider()) {
            return false;
        }

        if (serviceClass.equals(bean.serviceClass)
                && !hasQualifier(key) && !hasQualifier(bean.key)) {
            return true;
        }

        if (serviceClass.isAssignableFrom(bean.serviceClass) && (key == null && bean.key == null || key != null && key.equals(bean.key))) {
            return true;
        }

        return false;
    }

    private boolean hasQualifier(InjectionKey key) {
        if (key == null || key.getQualifier() == null) {
            return false;
        }
        return true;

    }

    public InjectionMetaData copy() {
        InjectionMetaData injectionMetaData = new InjectionMetaData(this.serviceClass, this.key, this.instanceCreator);
        injectionMetaData.setConstructor(this.constructor);
        injectionMetaData.setConstructorDependencies(this.constructorDependencies);
        injectionMetaData.setInjectionPoints(this.injectionPoints);
        injectionMetaData.setPreDefined(this.preDefined);
        injectionMetaData.setScopeHandler(InjectionUtils.getScopeHandler(injectionMetaData.getServiceClass()));
        injectionMetaData.setPostConstructMethod(this.postConstruct);
        return injectionMetaData;
    }
}
