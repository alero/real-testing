package org.hrodberaht.injection.internal.annotation.scope;

/**
 * Projectname
 *
 * @author Robert Alexandersson
 *         2010-sep-26 22:03:48
 * @version 1.0
 * @since 1.0
 */
public interface VariableScopeHandler extends ScopeHandler {
    Class getInstanceClass(Object variable);
}
