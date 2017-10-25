/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.exception;

import java.text.MessageFormat;


public class JUnitRuntimeException extends RuntimeException {
    private transient final Object[] args;


    public JUnitRuntimeException(String message) {
        super(message);
        this.args = null;
    }

    public JUnitRuntimeException(String message, Throwable e) {
        super(message, e);
        this.args = null;
    }

    public JUnitRuntimeException(Throwable e) {
        super(e);
        this.args = null;
    }

    public JUnitRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public JUnitRuntimeException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public JUnitRuntimeException(Throwable e, Object... args) {
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
