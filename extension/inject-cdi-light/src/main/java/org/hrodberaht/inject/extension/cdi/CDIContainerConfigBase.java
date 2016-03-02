package org.hrodberaht.inject.extension.cdi;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.config.InjectionRegisterScanBase;
import org.hrodberaht.inject.extension.cdi.inner.ContainerConfigBase;
import org.hrodberaht.inject.extension.cdi.inner.InjectionRegisterScanCDI;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.internal.annotation.ReflectionUtils;
import org.hrodberaht.inject.spi.ResourceCreator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
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
public abstract class CDIContainerConfigBase extends ContainerConfigBase<InjectionRegisterScanCDI> {


    protected ResourceCreator resourceCreator = null;
    private Map<String, EntityManager> entityManagers = null;
    private boolean hasPersistenceContextInClassPath = true;

    protected CDIContainerConfigBase(ResourceCreator resourceCreator) {
        this.resourceCreator = resourceCreator;
        initInjectionPoint();
    }

    protected void copyOriginalRegistryToActive() {
        activeRegister = originalRegister;
    }

    protected CDIContainerConfigBase() {
        initInjectionPoint();
    }

    private void initInjectionPoint() {
        super.injectionFinder = new DefaultInjectionPointFinder(this) {
            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                CDIContainerConfigBase config = (CDIContainerConfigBase) getContainerConfig();
                if (config != null) {
                    // System.out.println("DefaultInjectionPointFinder injecting resources");
                    config.injectResources(service);
                } else {
                    System.out.println("DefaultInjectionPointFinder NOT injecting resources due to config value null");
                }
            }
        };
        // InjectionPointFinder.setInjectionFinder(finder);
    }

    protected DataSource createDataSource(String dataSourceName) {
        return resourceCreator.createDataSource(dataSourceName);
    }

    public Collection<EntityManager> getEntityManagers() {
        return entityManagers.values();
    }

    public abstract InjectContainer createContainer();

    @Override
    protected InjectionRegisterScanBase getScanner() {
        return new InjectionRegisterScanCDI();
    }

    protected void addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<String, EntityManager>();
        }
        entityManagers.put(name, entityManager);
    }

    protected void injectResources(Object serviceInstance) {

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
                if (hasPersistenceContextInClassPath) {
                    try {
                        if (field.isAnnotationPresent(PersistenceContext.class)) {
                            PersistenceContext resource = field.getAnnotation(PersistenceContext.class);
                            injectEntityManager(serviceInstance, field, resource);
                        }
                    } catch (NoClassDefFoundError e) {
                        hasPersistenceContextInClassPath = false;
                    }
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


    public ResourceCreator getResourceCreator() {
        return resourceCreator;
    }

}
