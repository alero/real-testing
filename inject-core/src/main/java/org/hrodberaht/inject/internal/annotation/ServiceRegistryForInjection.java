package org.hrodberaht.inject.internal.annotation;

import org.hrodberaht.inject.internal.InjectionKey;
import org.hrodberaht.inject.internal.ServiceRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexbrob on 2016-02-17.
 */
public class ServiceRegistryForInjection {

    private Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache = null;

    public ServiceRegistryForInjection(Map<InjectionKey, InjectionMetaDataBase> injectionMetaDataCache) {
        this.injectionMetaDataCache = injectionMetaDataCache;
    }

    public Collection<ServiceRegister> getServiceRegisterCollection() {
        Collection<ServiceRegister> registers = new ArrayList<>();
        for(InjectionMetaDataBase injectionMetaDataBase:injectionMetaDataCache.values()){
            if(injectionMetaDataBase.getServiceRegister() != null){
                registers.add(injectionMetaDataBase.getServiceRegister());
            }
        }
        return registers;
    }

    public ServiceRegister get(InjectionKey key) {
        InjectionMetaDataBase injectionMetaDataBase = injectionMetaDataCache.get(key);
        if(injectionMetaDataBase != null){
            return injectionMetaDataBase.getServiceRegister();
        }
        return null;
    }

    public boolean containsKey(InjectionKey key) {
        return injectionMetaDataCache.containsKey(key);
    }

    public Set<InjectionKey> keySet() {
        return injectionMetaDataCache.keySet();
    }

    public void put(InjectionKey injectionKey, ServiceRegister clone) {
        injectionMetaDataCache.get(injectionKey).setServiceRegister(clone);
    }
}
