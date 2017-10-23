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

package org.hrodberaht.injection.plugin.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextManager {
    public ContextManager() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactoryImpl.class.getName());
    }

    public void bind(String name, Object instance) {
        try {
            Context context = new InitialContext();
            context.bind(name, instance);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
