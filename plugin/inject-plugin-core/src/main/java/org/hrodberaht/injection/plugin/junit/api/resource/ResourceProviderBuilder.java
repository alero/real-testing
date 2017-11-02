package org.hrodberaht.injection.plugin.junit.api.resource;

import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

public class ResourceProviderBuilder implements ResourceProviderSupport{

    private final Set<ResourceProvider> resourceProviders = new HashSet<>();

    private ResourceProviderBuilder(){
    }

    public static ResourceProviderBuilder of() {
        return new ResourceProviderBuilder();
    }

    public ResourceProviderBuilder resource(String name, Provider instance){
        resourceProviders.add(new ResourceProvider(name, null, instance));
        return this;
    }

    public ResourceProviderBuilder resource(Class type, Provider instance){
        resourceProviders.add(new ResourceProvider(null, type, instance));
        return this;
    }

    public ResourceProviderBuilder resource(String name, Class type, Provider instance){
        resourceProviders.add(new ResourceProvider(name, type, instance));
        return this;
    }

    @Override
    public Set<ResourceProvider> resources() {
        return resourceProviders;
    }
}
