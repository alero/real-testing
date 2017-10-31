package org.hrodberaht.injection.plugin.junit.api.resource;

import javax.inject.Provider;

public class ResourceProvider {

    private final String name;
    private final Class type;
    private final Provider instanceProvider;

    public ResourceProvider(String name, Class type, Provider instanceProvider) {
        this.name = name;
        this.type = type;
        this.instanceProvider = instanceProvider;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Provider getInstanceProvider() {
        return instanceProvider;
    }

    public Object getInstance() {
        return instanceProvider.get();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceProvider that = (ResourceProvider) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
