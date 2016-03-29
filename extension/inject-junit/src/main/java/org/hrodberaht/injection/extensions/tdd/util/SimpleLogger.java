package org.hrodberaht.injection.extensions.tdd.util;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2013-02-12
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public class SimpleLogger {

    private static boolean enabled = false;

    static {
        if ("enabled".equals(System.getProperty("inject.tdd-ext.logger"))) {
            enabled = true;
        }
    }

    public static void log(String message) {
        if (enabled) {
            System.out.println(message);
        }
    }


}
