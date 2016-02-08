package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorCGLIB;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorDefault;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jul-15 22:54:16
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorFactory {

    private static InstanceCreator creator = null;

    static {
        initiateFactory();
    }

    private static void initiateFactory() {
        if (System.getProperty("simpleinjection.instancecreator.cglib") != null) {
            InstanceCreatorFactory.creator = new InstanceCreatorCGLIB();
        } else {
            InstanceCreatorFactory.creator = new InstanceCreatorDefault();
        }
    }

    private InstanceCreatorFactory() {
    }

    public static void setCreator(InstanceCreator creator) {
        InstanceCreatorFactory.creator = creator;
    }

    public static void resetCreator() {
        initiateFactory();
    }

    static InstanceCreator getInstance() {
        return InstanceCreatorFactory.creator;
    }
}
