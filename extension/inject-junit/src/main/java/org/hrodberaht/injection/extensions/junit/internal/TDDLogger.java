package org.hrodberaht.injection.extensions.junit.internal;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-02-05 20:11
 * @created 1.0
 * @since 1.0
 */
public class TDDLogger {


    public static void log(String message) {
        if (System.getProperty("hrodberaht.junit.log") != null) {
            System.out.println(message);
        }
    }

}
