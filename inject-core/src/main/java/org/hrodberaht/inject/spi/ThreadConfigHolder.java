package org.hrodberaht.inject.spi;

/**
 * Injection
 *
 * @author Robert Alexandersson
 *         2011-05-22 17:43
 * @created 1.0
 * @since 1.0
 */
@Deprecated
public class ThreadConfigHolder {

    private static final InheritableThreadLocal<ContainerConfig> threadLocal = new InheritableThreadLocal<ContainerConfig>();


    public static ContainerConfig get() {
        return threadLocal.get();
    }

    public static void set(ContainerConfig configBase) {
        threadLocal.set(configBase);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
