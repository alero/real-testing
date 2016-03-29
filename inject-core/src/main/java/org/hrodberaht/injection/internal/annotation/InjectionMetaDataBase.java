package org.hrodberaht.injection.internal.annotation;

import org.hrodberaht.injection.internal.ServiceRegister;

/**
 * Created by alexbrob on 2016-02-17.
 */
public class InjectionMetaDataBase {

    private InjectionMetaData injectionMetaData;
    private ServiceRegister serviceRegister;

    public InjectionMetaDataBase(InjectionMetaData injectionMetaData) {
        this.injectionMetaData = injectionMetaData;
    }

    public InjectionMetaDataBase(InjectionMetaData injectionMetaData, ServiceRegister serviceRegister) {
        this.injectionMetaData = injectionMetaData;
        this.serviceRegister = serviceRegister;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }

    public InjectionMetaData getInjectionMetaData() {
        return injectionMetaData;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public InjectionMetaDataBase clone() throws CloneNotSupportedException {
        return new InjectionMetaDataBase(injectionMetaData.clone(), serviceRegister.clone());
    }
}
