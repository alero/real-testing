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

package org.hrodberaht.injection.internal.annotation.creator;

import org.hrodberaht.injection.internal.exception.InstanceCreationError;
import org.hrodberaht.injection.internal.stats.Statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 00:18:20
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorDefault implements InstanceCreator {

    public Object createInstance(Constructor constructor, Object... parameters) {
        try {
            Statistics.addNewInstanceCount();
            return constructor.newInstance(parameters);

        } catch (InstantiationException e) {
            throw new InstanceCreationError(e);
        } catch (IllegalAccessException e) {
            throw new InstanceCreationError(e);
        } catch (InvocationTargetException e) {
            throw new InstanceCreationError(e);
        } catch (IllegalArgumentException e) {
            throw new InstanceCreationError(e);
        } catch (NullPointerException e) {
            throw new InstanceCreationError(e);
        }
    }
}
