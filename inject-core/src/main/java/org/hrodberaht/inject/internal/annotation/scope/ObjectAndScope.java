package org.hrodberaht.inject.internal.annotation.scope;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-10-31
 * Time: 08:11
 * To change this template use File | Settings | File Templates.
 */
public class ObjectAndScope {

    private Object instance;
    private boolean newObject;

    public ObjectAndScope(Object instance, boolean newObject) {
        this.instance = instance;
        this.newObject = newObject;
    }

    public boolean isInNeedOfInitialization() {
        return newObject;
    }

    public Object getInstance() {
        return instance;
    }
}
