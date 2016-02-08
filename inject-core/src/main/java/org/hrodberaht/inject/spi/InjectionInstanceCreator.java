package org.hrodberaht.inject.spi;

import org.hrodberaht.inject.internal.annotation.InstanceCreatorFactory;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:10:19
 * @version 1.0
 * @since 1.0
 */
public class InjectionInstanceCreator {
    public static void changeInstanceCreator(InstanceCreator instanceCreator) {
        InstanceCreatorFactory.setCreator(instanceCreator);
    }

    public static void resetInstanceCreator() {
        InstanceCreatorFactory.resetCreator();
    }
}
