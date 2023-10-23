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

package org.hrodberaht.injection.annotation;

import org.hrodberaht.injection.testservices.annotated.SpecialResource;
import org.hrodberaht.injection.core.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

/**
 * Automated documentation
 *
 * @author Robert Alexandersson
 * 2011-04-04 21:23
 * @created 1.0
 * @since 1.0
 */
public class ExtendedResourceInjection {
    public static void injectText(Object serviceInstance, String value) {
        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(SpecialResource.class)) {
                    try {
                        field.setAccessible(true);
                        field.set(serviceInstance, value);
                    } catch (IllegalAccessException e) {
                        throw new InjectRuntimeException("Bad injection ");
                    }
                }
            }
        }
    }
}
