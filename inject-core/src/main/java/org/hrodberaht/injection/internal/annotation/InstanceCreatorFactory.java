package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.injection.internal.annotation.creator.InstanceCreatorDefault;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jul-15 22:54:16
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorFactory {

    private InstanceCreator creator = new InstanceCreatorDefault();


    private InstanceCreatorFactory() {
    }


    InstanceCreator getInstance() {
        return this.creator;
    }
}
