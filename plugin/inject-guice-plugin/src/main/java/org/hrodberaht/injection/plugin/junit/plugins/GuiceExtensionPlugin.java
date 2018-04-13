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

package org.hrodberaht.injection.plugin.junit.plugins;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.hrodberaht.injection.core.internal.InjectionKey;
import org.hrodberaht.injection.core.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.injection.core.internal.annotation.InjectionFinder;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.core.register.RegistrationModuleAnnotation;
import org.hrodberaht.injection.core.spi.ContainerConfigBuilder;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionFinder;
import org.hrodberaht.injection.plugin.junit.api.annotation.InjectionPluginInjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProvider;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProviderSupport;
import org.hrodberaht.injection.plugin.junit.plugins.common.PluginLifeCycledResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GuiceExtensionPlugin implements Plugin {
    private static final Logger LOG = LoggerFactory.getLogger(GuiceExtensionPlugin.class);
    private Injector injector;
    private List<Module> guiceModules = new ArrayList<>();
    private Set<ResourceProvider> resourceProviders = new HashSet<>();
    private PluginLifeCycledResource<Injector> pluginLifeCycledResource = new PluginLifeCycledResource<>(Injector.class);
    private final LifeCycle lifeCycle;

    private Map<Class, Injector> loadedModules = new ConcurrentHashMap<>();


    public GuiceExtensionPlugin() {
        lifeCycle = LifeCycle.TEST_CLASS;
    }

    public GuiceExtensionPlugin(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @InjectionPluginInjectionRegister
    protected void setInjectionRegister(InjectionRegister injectionRegister) {
        if (injector != null) {
            injectionRegister.register(new RegistrationModuleAnnotation() {
                @Override
                public void registrations() {
                    register(Injector.class).withFactoryInstance(injector);
                }
            });
        }
    }

    @InjectionPluginInjectionFinder
    protected InjectionFinder getInjectionFinder(ContainerConfigBuilder containerConfigBuilder) {
        return new DefaultInjectionPointFinder(containerConfigBuilder) {

            @Override
            public Object extendedInjection(InjectionKey injectionKey) {
                if (injectionKey.getAnnotation() != null){
                    Key key = Key.get(injectionKey.getServiceDefinition(), injectionKey.getAnnotation());
                    return injector.getInstance(key);
                }else{
                    Key key = Key.get(injectionKey.getServiceDefinition());
                    return injector.getInstance(key);
                }
            }
        };
    }

    public GuiceExtensionPlugin with(Plugin plugin){
        if(plugin instanceof ResourceProviderSupport) {
            resourceProviders.addAll(((ResourceProviderSupport)plugin).resources());
        }
        return this;
    }

    public GuiceExtensionPlugin withResources(ResourceProviderSupport resourceProviderSupport){
        resourceProviders.addAll(resourceProviderSupport.resources());
        return this;
    }

    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        LOG.info("beforeContainerCreation for {}", this);
        injector = pluginLifeCycledResource.create(lifeCycle, pluginContext, () -> createInjector(pluginContext, guiceModules));
    }

    public Injector getInjector() {
        return injector;
    }

    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }


    public GuiceExtensionPlugin guiceModules(Module... modules) {
        guiceModules.addAll(Arrays.asList(modules));
        return this;
    }

    private Injector createInjector(final PluginContext pluginContext, List<Module> modules) {
        return loadedModules.computeIfAbsent(getKey(pluginContext), aClass -> {
            if (!resourceProviders.isEmpty()) {
                Module module = binder -> resourceProviders.forEach(resourceProvider -> {
                    if (resourceProvider.getName() == null) {
                        binder.bind(resourceProvider.getType()).toInstance(resourceProvider.getInstance());
                    } else {
                        binder.bind(resourceProvider.getType()).annotatedWith(Names.named(resourceProvider.getName())).toInstance(resourceProvider.getInstance());
                    }
                });
                modules.add(module);
            }
            return Guice.createInjector(modules);
        });
    }

    private Class getKey(PluginContext pluginContext) {
        if (lifeCycle == LifeCycle.TEST_CONFIG) {
            return pluginContext.getConfigClass();
        } else if (lifeCycle == LifeCycle.TEST_CLASS) {
            return pluginContext.getTestClass();
        } else if (lifeCycle == LifeCycle.TEST_SUITE) {
            return GuiceExtensionPlugin.class;
        }
        throw new UnsupportedOperationException("LifeCycle is not supported by Plugin : " + lifeCycle);
    }

    public GuiceExtensionPlugin guiceModules(List<Module> modules) {
        guiceModules.addAll(modules);
        return this;
    }
}
