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

import org.hrodberaht.injection.internal.exception.InjectRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-maj-29 00:58:46
 * @version 1.0
 * @since 1.0
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static List<Method> findMethods(Class clazz) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final List<Method> methods = new ArrayList<Method>();

        methods.addAll(Arrays.asList(declaredMethods));

        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            methods.addAll(findMethods(clazz.getSuperclass()));
        }

        return methods;
    }


    public static List<Member> findMembers(Class clazz) {
        final List<Member> members = new ArrayList<Member>();

        final Field[] declaredFields = clazz.getDeclaredFields();

        members.addAll(Arrays.asList(declaredFields));

        final Method[] declaredMethods = clazz.getDeclaredMethods();

        members.addAll(Arrays.asList(declaredMethods));


        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            members.addAll(0, findMembers(clazz.getSuperclass()));
        }

        return members;
    }

    public static boolean isOverridden(Method method, List<Method> candidates) {
        for (final Method candidate : candidates) {
            if (isOverridden(method, candidate)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isOverridden(Method method, Method candidate) {
        if (!hasOverridableAccessModifiers(method, candidate)) {
            return false;
        }

        if (!isSubClassOf(candidate.getDeclaringClass(), method.getDeclaringClass())) {
            return false;
        }

        if (!hasTheSameName(method, candidate)) {
            return false;
        }

        if (!hasTheSameParameters(method, candidate)) {
            return false;
        }

        return true;
    }

    public static boolean isInTheSamePackage(Method method, Method candidate) {
        final Package methodPackage = method.getDeclaringClass().getPackage();
        final Package candidatePackage = candidate.getDeclaringClass().getPackage();

        return methodPackage.equals(candidatePackage);
    }


    public static boolean hasOverridableAccessModifiers(Method method, Method candidate) {
        if (isFinal(method) || isPrivate(method) || isStatic(method)
                || isPrivate(candidate) || isStatic(candidate)) {
            return false;
        }

        if (isDefault(method)) {
            return isInTheSamePackage(method, candidate);
        }

        return true;
    }

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }

    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isDefault(Member member) {
        return !isPublic(member) && !isProtected(member) && !isPrivate(member);
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isSubClassOf(Class<?> subclass, Class<?> superclass) {
        if (subclass.getSuperclass() != null) {
            if (subclass.getSuperclass().equals(superclass)) {
                return true;
            }

            return isSubClassOf(subclass.getSuperclass(), superclass);
        }

        return false;
    }

    public static boolean hasTheSameName(Method method, Method candidate) {
        return method.getName().equals(candidate.getName());
    }

    public static boolean hasTheSameParameters(Method method, Method candidate) {
        Class<?>[] methodParameters = method.getParameterTypes();
        Class<?>[] candidateParameters = candidate.getParameterTypes();

        if (methodParameters.length != candidateParameters.length) {
            return false;
        }

        for (int i = 0; i < methodParameters.length; i++) {
            Class<?> methodParameter = methodParameters[i];
            Class<?> candidateParameter = candidateParameters[i];

            if (!methodParameter.equals(candidateParameter)) {
                return false;
            }
        }

        return true;
    }

    public static void invoke(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException e) {
            throw new InjectRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InjectRuntimeException(e);
        }
    }
}
