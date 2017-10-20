package org.hrodberaht.injection.internal.annotation.creator;

import java.lang.reflect.Constructor;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * 2010-jun-06 00:16:21
 * @version 1.0
 * @since 1.0
 */
public interface InstanceCreator {
    Object createInstance(Constructor constructor, Object... parameters);
}
