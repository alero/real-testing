package org.hrodberaht.inject;

import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by alexbrob on 2015-01-05.
 */
public class ClassScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);

    private Collection<CustomClassLoader> customClassLoaders = new ArrayList<CustomClassLoader>();
    private boolean detailedScanLogging = false;

    public Collection<CustomClassLoader> getCustomClassLoaders() {
        return customClassLoaders;
    }

    public void setDetailedScanLogging(boolean detailedScanLogging) {
        this.detailedScanLogging = detailedScanLogging;
    }

    public boolean isDetailedScanLogging() {
        return detailedScanLogging;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public Class[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class> classes = findClassesToLoad(
                packageName, classLoader, CustomClassLoader.ClassLoaderType.THREAD
        );
        for (CustomClassLoader customclassLoader : customClassLoaders) {
            ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();
            classes.addAll(findClassesToLoad(packageName, parentClassLoader, customclassLoader.loaderType));
        }
        return classes.toArray(new Class[classes.size()]);

    }

    public void createRegistration(Class aClazz, InjectionContainerManager container) {
        if (
                !aClazz.isInterface()
                        && !aClazz.isAnnotation()
                        && !Modifier.isAbstract(aClazz.getModifiers())
                ) {
            try {
                container.register(aClazz, aClazz, null, InjectionContainerManager.RegisterType.NORMAL);
            } catch (InjectRuntimeException e) {
                LOG.info("Hrodberaht Injection: Silently failed to register class = " + aClazz);
                if (detailedScanLogging) {
                    LOG.error("Failed registering a class = " + aClazz, e);
                }
            }
        }
    }


    private ArrayList<Class> findClassesToLoad(
            String packageName, ClassLoader classLoader, CustomClassLoader.ClassLoaderType loaderType) {
        if (loaderType == CustomClassLoader.ClassLoaderType.THREAD) {
            return findFiles(packageName, classLoader);
        } else if (loaderType == CustomClassLoader.ClassLoaderType.JAR) {
            return findFiles(packageName, classLoader);
        }
        return null;
    }


    private ArrayList<Class> findFiles(String packageName, ClassLoader classLoader) {
        ArrayList<Class> classes = new ArrayList<Class>();
        try {

            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
            }

            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        } catch (IOException e) {
            throw new InjectRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
                );
            }
        }
        return classes;
    }


    private static class CustomClassLoader {

        private enum ClassLoaderType {JAR, THREAD}

        ;

        public CustomClassLoader(URLClassLoader classLoader, ClassLoaderType loaderType) {
            this.classLoader = classLoader;
            this.loaderType = loaderType;
        }

        private ClassLoader classLoader;
        private ClassLoaderType loaderType;
    }

}
