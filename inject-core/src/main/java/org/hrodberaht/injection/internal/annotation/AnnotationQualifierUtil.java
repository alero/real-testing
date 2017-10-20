/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.internal.InjectionKey;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-28 21:50:56
 * @version 1.0
 * @since 1.0
 */
public class AnnotationQualifierUtil {

    private AnnotationQualifierUtil() {
    }

    private static final Class<Qualifier> QUALIFIER = Qualifier.class;

    public static InjectionKey getQualifierKey(Class owner, Annotation[] annotations, boolean provider) {
        final List<InjectionKey> qualifierAnnotations = new ArrayList<InjectionKey>();

        for (final Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(QUALIFIER)) {
                qualifierAnnotations.add(getQualifier(owner, annotation, provider));
            }
        }
        if (qualifierAnnotations.size() == 0) {
            return null;
        } else if (qualifierAnnotations.size() > 1) {
            throw new InjectRuntimeException(
                    "Several qualifier annotations found on " + owner + " " + qualifierAnnotations);
        }

        return qualifierAnnotations.get(0);
    }

    private static InjectionKey getQualifier(Class owner, Annotation annotation, boolean provider) {
        if (annotation instanceof Named) {
            Named named = (Named) annotation;
            String value = named.value();
            if (isEmpty(value)) {
                throw new InjectRuntimeException("Named qualifier annotation used without a value " + owner);
            }
            return new InjectionKey(value, owner, provider);
        } else {
            return new InjectionKey(annotation.annotationType(), owner, provider);
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

}
