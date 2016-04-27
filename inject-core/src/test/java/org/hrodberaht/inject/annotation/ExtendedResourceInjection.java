package org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.testservices.annotated.SpecialResource;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-04-04 21:23
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
