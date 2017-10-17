package org.hrodberaht.injection.spi;

public class ResourceKey {
    private final String name;
    private final Class type;


    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public static ResourceKey of(String name, Class type){
        return new ResourceKey(name, type);
    }

    private ResourceKey(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceKey key = (ResourceKey) o;

        if (name != null ? !name.equals(key.name) : key.name != null) return false;
        return type != null ? type.equals(key.type) : key.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResourceKey{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
