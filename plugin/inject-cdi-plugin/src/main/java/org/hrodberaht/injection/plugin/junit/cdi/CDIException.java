package org.hrodberaht.injection.plugin.junit.cdi;

public class CDIException extends RuntimeException {

    public CDIException(String message, Throwable cause) {
        super(message, cause);
    }

    public CDIException(Throwable cause) {
        super(cause);
    }

    public CDIException(String message) {
        super(message);
    }
}
