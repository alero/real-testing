package org.hrodberaht.inject.extension.tdd.ejb;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.tdd.ContainerConfigBase;
import org.hrodberaht.inject.extension.tdd.ejb.internal.InjectionRegisterScanEJB;
import org.hrodberaht.inject.extension.tdd.ejb.internal.SessionContextCreator;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.internal.annotation.ReflectionUtils;
import org.hrodberaht.inject.spi.InjectionPointFinder;
import org.hrodberaht.inject.spi.ThreadConfigHolder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class TDDEJBContainerConfigBase extends ContainerConfigBase<InjectionRegisterScanEJB> {

    private Map<String, EntityManager> entityManagers = null;

    protected TDDEJBContainerConfigBase() {
        DefaultInjectionPointFinder finder = new DefaultInjectionPointFinder() {
            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                return super.hasInjectAnnotationOnMethod(method);
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                return field.isAnnotationPresent(EJB.class) ||
                        super.hasInjectAnnotationOnField(field);
            }

            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                TDDEJBContainerConfigBase config = (TDDEJBContainerConfigBase) ThreadConfigHolder.get();
                config.injectResources(service);
            }
        };
        InjectionPointFinder.setInjectionFinder(finder);
    }

    public Collection<EntityManager> getEntityManagers() {
        return entityManagers.values();
    }

    public abstract InjectContainer createContainer();

    @Override
    protected InjectionRegisterScanEJB getScanner() {
        return new InjectionRegisterScanEJB();
    }

    protected void addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<String, EntityManager>();
        }
        entityManagers.put(name, entityManager);
    }

    protected EntityManager createEntityManager(String schemaName, String dataSourceName, DataSource dataSource) {
        return getResourceCreator().createEntityManager(schemaName, dataSourceName, dataSource);
    }

    protected void injectResources(Object serviceInstance) {
        if (serviceInstance instanceof SessionBean) {
            SessionBean sessionBean = (SessionBean) serviceInstance;
            try {
                sessionBean.setSessionContext(SessionContextCreator.create());
            } catch (RemoteException e) {
                // Nope
            }
        }

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
