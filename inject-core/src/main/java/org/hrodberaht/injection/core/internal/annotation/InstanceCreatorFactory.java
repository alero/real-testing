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

import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.core.internal.annotation.creator.InstanceCreatorDefault;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-jul-15 22:54:16
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorFactory {

    private InstanceCreator creator = new InstanceCreatorDefault();


    private InstanceCreatorFactory() {
    }


    InstanceCreator getInstance() {
        return this.creator;
    }
}
