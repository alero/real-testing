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
package org.hrodberaht.injection.core

import org.hrodberaht.injection.core.config.InjectionStoreFactory

/**
 * Created by alexbrob on 2016-03-29.
 */
class InjectionRegistryPlain : InjectionRegistry<Module?> {
    private var injectionContainer: InjectContainer? = null
    private val injectionRegister = InjectionStoreFactory.getInjectionRegister()
    fun register(module: Module?): InjectionRegistryPlain {
        injectionRegister.register(module)
        injectionContainer = injectionRegister.container
        return this
    }

    override fun getContainer(): InjectContainer {
        return injectionContainer!!
    }

    override fun getModule(): Module {
        val module = Module(this.injectionContainer)
        injectionRegister.fillModule(module)
        return module
    }
}