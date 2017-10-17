package org.hrodberaht.injection.extensions.plugin.junit;

import org.hrodberaht.injection.spi.ContainerConfig;
import org.hrodberaht.injection.spi.ContainerConfigBuilder;

import java.lang.annotation.*;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:34:24
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ContainerContext {
    Class<? extends ContainerConfigBuilder> value();
}
