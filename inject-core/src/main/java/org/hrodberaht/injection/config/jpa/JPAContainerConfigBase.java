package org.hrodberaht.injection.config.jpa;

import org.hrodberaht.injection.InjectionRegisterModule;
import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.spi.ResourceCreator;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class JPAContainerConfigBase<T extends InjectionRegisterModule> extends ContainerConfigBase<T> {

    protected Map<String, EntityManager> entityManagers = null;

    public Collection<EntityManager> getEntityManagers() {
        if(entityManagers == null){
            return new ArrayList<>();
        }
        return entityManagers.values();
    }

    public ResourceCreator<EntityManager, ?> getResourceCreator() {
        return resourceCreator;
    }

    protected EntityManager createEntityManager(String schemaName, String dataSourceName, DataSource dataSource) {
        return getResourceCreator().createEntityManager(schemaName, dataSourceName, dataSource);
    }

    protected void injectGenericResources(Object serviceInstance) {
        if (resources == null && entityManagers == null && typedResources == null) {
            return;
        }

        List<Member> members = ReflectionUtils.findMembers(serviceInstance.getClass());
        for (Member member : members) {
            if (member instanceof Field) {
                Field field = (Field) member;
                if (field.isAnnotationPresent(Resource.class)) {
                    Resource resource = field.getAnnotation(Resource.class);
                    if (!injectNamedResource(serviceInstance, field, resource)) {
                        injectTypedResource(serviceInstance, field);
                    }
                }
                if (field.isAnnotationPresent(PersistenceContext.class)) {
                    PersistenceContext resource = field.getAnnotation(PersistenceContext.class);
                    injectEntityManager(serviceInstance, field, resource);
                }
            }
        }
    }

    private void injectEntityManager(Object serviceInstance, Field field, PersistenceContext resource) {
        Object value = entityManagers.get(resource.unitName());
        if (value == null && entityManagers.size() == 1 && "".equals(resource.unitName())) {
            value = entityManagers.values().iterator().next();
        }
        injectResourceValue(serviceInstance, field, value);
    }


}
