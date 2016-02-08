package javax.ejb;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:22:59
 * @version 1.0
 * @since 1.0
 */
public enum TransactionAttributeType {
    REQUIRES_NEW, REQUIRED, SUPPORTS, MANDATORY, NOT_SUPPORTED, NEVER 
}
