package org.hrodberaht.injection.config;

import org.hrodberaht.injection.ScopeContainer;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class ClassScanningUtil {

    private static boolean detailedScanLogging = false;

    private static final Logger LOG = LoggerFactory.getLogger(ClassScanningUtil.class);

    public static void setDetailedScanLogging(boolean detailedScanLogging) {
        ClassScanningUtil.detailedScanLogging = detailedScanLogging;
    }

    public static Class findServiceImplementation(Class theInterface, List<Class> listOfClasses) {

        Class foundServiceImplementation = null;
        for (Class aServiceClass : listOfClasses) {
            if (!aServiceClass.isInterface()
                    && !aServiceClass.isAnnotation()
                    && !Modifier.isAbstract(aServiceClass.getModifiers())
                    ) {
                for (Class aInterface : aServiceClass.getInterfaces()) {
                    if (aInterface == theInterface) {
                        if (foundServiceImplementation != null) {
                            throw new InjectRuntimeException("ServiceInterface implemented in two classes {0} and {1}"
                                    , foundServiceImplementation, aServiceClass
                            );
                        }
                        foundServiceImplementation = aServiceClass;
                    }
                }
            }
        }

        return foundServiceImplementation;
    }

    public static void createRegistration(Class aClazz, List<Class> listOfClasses, ScanningService scanningService) {
        if (
                aClazz.isInterface()
                        && !aClazz.isAnnotation()
                        && scanningService.isInterfaceAnnotated(aClazz)
                ) {
            try {
                Class serviceClass = ClassScanningUtil.findServiceImplementation(aClazz, listOfClasses);
                if (serviceClass != null) {
                    scanningService.registerForScanner(aClazz, serviceClass, scanningService.getScope(serviceClass));
                }
            } catch (InjectRuntimeException e) {
                LOG.debug("Hrodberaht Injection: Silently failed to register class for interface= " + aClazz);
                if (detailedScanLogging) {
                    e.printStackTrace(System.err);
                }
            }
        }
        else if (
                !aClazz.isInterface()
                        && !aClazz.isAnnotation()
                        && scanningService.isServiceAnnotated(aClazz)
                ) {
            try {
                Class[] interfaces = aClazz.getInterfaces();
                ScopeContainer.Scope scope = scanningService.getScope(aClazz);
                for (Class _interface : interfaces) {
                    scanningService.registerForScanner(_interface, aClazz, scope);
                }
            } catch (InjectRuntimeException e) {
                LOG.debug("Hrodberaht Injection: Silently failed to register class for service= " + aClazz);
                if (detailedScanLogging) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }


}
