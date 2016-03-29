package org.hrodberaht.injection.config;

import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by alexbrob on 2015-01-05.
 */
public class ClassScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);

    public List<Class> getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class> classes = findClassesToLoad(
                packageName, classLoader, CustomClassLoader.ClassLoaderType.THREAD
        );
        classes.addAll(findClassesToLoad(packageName, classLoader, CustomClassLoader.ClassLoaderType.JAR));

        return classes;
    }

    private ArrayList<Class> findClassesToLoad(
            String packageName, ClassLoader classLoader, CustomClassLoader.ClassLoaderType loaderType) {
        if (loaderType == CustomClassLoader.ClassLoaderType.THREAD) {
            return findFiles(packageName, classLoader);
        } else if (loaderType == CustomClassLoader.ClassLoaderType.JAR) {
            return findJarFiles(packageName, classLoader);
        }
        throw new IllegalAccessError("No classloader type defined");
    }

    private ArrayList<Class> findJarFiles(String packageName, ClassLoader classLoader) {

        try {
            List<File> filesToLoad = JarUtil.findTheJarFiles(packageName, classLoader);

            if (filesToLoad == null) {
                return new ArrayList<Class>();
            }
            ArrayList<Class> classes = new ArrayList<Class>(200);
            for (File fileToLoad : filesToLoad) {
                LOG.debug("findJarFiles fileToLoad = " + fileToLoad);
                JarFile jarFile = new JarFile(fileToLoad);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = enumeration.nextElement();
                    String classPath = jarEntry.getName().replaceAll("/", ".");
                    if (!jarEntry.isDirectory() && classPath.startsWith(packageName) && classPath.endsWith(".class")) {
                        String classPathName = classPath.substring(0, classPath.length() - 6);
                        try {
                            Class aClass = Class.forName(classPathName);
                            LOG.debug("jar aClass: " + aClass + " for " + fileToLoad.getName());
                            classes.add(aClass);
                        } catch (ClassNotFoundException e) {
                            LOG.info("jar error lookup: " + classPathName);
                            throw e;
                        }
                    }
                }
            }
            return classes;  //To change body of created methods use File | Settings | File Templates.
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CustomClassLoader {
        private enum ClassLoaderType {
            JAR, THREAD
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */

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
        if (files == null) {
            return null;
        }
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

}
