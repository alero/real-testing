package javax.ejb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:22:00
 * @version 1.0
 * @since 1.0
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionAttribute {
  TransactionAttributeType value() default javax.ejb.TransactionAttributeType.REQUIRED;
}
