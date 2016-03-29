package com.hrodberaht.inject.extensions.transaction.manager.internal.vendor;

import javax.persistence.EntityManager;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-27 15:07:27
 * @version 1.0
 * @since 1.0
 */
public class ProviderFactory {

    public static ProviderService getService(EntityManager entityManager){
        String className = entityManager.getClass().getName();
        if(className.equals("org.hibernate.ejb.EntityManagerImpl")){
            return new HibernateProviderService();
        }
        else if(className.equals("org.eclipse.persistence.internal.jpa.EntityManagerImpl")){
            return new EclipseLinkProviderService();
        }
        else if(className.equals("org.apache.openjpa.persistence.EntityManagerImpl")){
            return new OpenJPAProviderService();
        }
        else if(className.equals("org.datanucleus.jpa.EntityManagerImpl")){
            return new DatanucleusProviderService();
        }
        return null;
    }


}
