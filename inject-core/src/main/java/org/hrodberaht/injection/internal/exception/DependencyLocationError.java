package org.hrodberaht.injection.internal.exception;

import java.text.MessageFormat;

public class DependencyLocationError extends RuntimeException {

    private transient final Object[] args;

    public DependencyLocationError(String message) {
        super(message);
        this.args = null;
    }

    public DependencyLocationError(String message, Throwable e) {
        super(message, e);
        this.args = null;
    }

    public DependencyLocationError(Throwable e) {
        super(e);
        this.args = null;
    }

    public DependencyLocationError(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public DependencyLocationError(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public DependencyLocationError(Throwable e, Object... args) {
        super(e);
        this.args = args;
    }

    @Override
    public String toString() {
        if (args != null) {
            return this.getClass().getName()
                    + ": " + MessageFormat.format(super.getMessage(), args);
        }
        return super.toString();
    }

    @Override
    public String getMessage() {
        if (args != null) {
            return MessageFormat.format(super.getMessage(), args);
        }
        return super.getMessage();
    }
}
