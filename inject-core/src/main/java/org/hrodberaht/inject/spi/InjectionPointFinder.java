package org.hrodberaht.inject.spi;

import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.internal.annotation.InjectionFinder;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-sep-23 20:38:01
 * @version 1.0
 * @since 1.0
 */
public class InjectionPointFinder {

    private static InjectionFinder injectionFinder = createDefaultInjectionFinder();

    private static InjectionFinder createDefaultInjectionFinder() {
        return new DefaultInjectionPointFinder();
    }

    public static InjectionFinder getInjectionFinder() {
        return injectionFinder;
    }

    public static void setInjectionFinder(InjectionFinder injectionFinder) {
        InjectionPointFinder.injectionFinder = injectionFinder;
    }

    public static void resetInjectionFinderToDefault() {
        InjectionPointFinder.injectionFinder = createDefaultInjectionFinder();
    }
}
