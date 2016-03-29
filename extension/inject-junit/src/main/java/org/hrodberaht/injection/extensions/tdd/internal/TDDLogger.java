package org.hrodberaht.injection.extensions.tdd.internal;

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
        if (System.getProperty("tdd.log") != null) {
            System.out.println(message);
        }
    }

}
