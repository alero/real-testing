/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.core.config;

import org.hrodberaht.injection.core.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by alexbrob on 2015-01-05.
 */
public class ClassScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);

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
            return Collections.emptyList();
        }
        for (File file : files) {
            if (file.isDirectory()) {
                if (!".".contains(file.getName())) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                }
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
                );
            }
        }
        return classes;
    }

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
                return new ArrayList<>();
            }
            ArrayList<Class> classes = new ArrayList<>(200);
            for (File fileToLoad : filesToLoad) {
                LOG.debug("findJarFiles fileToLoad = {}", fileToLoad);
                try (JarFile jarFile = new JarFile(fileToLoad)) {
                    Enumeration<JarEntry> enumeration = jarFile.entries();
                    while (enumeration.hasMoreElements()) {
                        manageJarEntries(packageName, classes, fileToLoad, enumeration);
                    }
                }
            }
            return classes;  //To change body of created methods use File | Settings | File Templates.
        } catch (IOException | ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
    }

    private void manageJarEntries(String packageName, ArrayList<Class> classes, File fileToLoad, Enumeration<JarEntry> enumeration) throws ClassNotFoundException {
        JarEntry jarEntry = enumeration.nextElement();
        String classPath = jarEntry.getName().replaceAll("/", ".");
        if (!jarEntry.isDirectory() && classPath.startsWith(packageName) && classPath.endsWith(".class")) {
            String classPathName = classPath.substring(0, classPath.length() - 6);
            try {
                Class aClass = Class.forName(classPathName);
                LOG.debug("jar aClass: {} for {}", aClass, fileToLoad.getName());
                classes.add(aClass);
            } catch (ClassNotFoundException e) {
                LOG.info("jar error lookup: {}", classPathName);
                throw e;
            }
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */

    private ArrayList<Class> findFiles(String packageName, ClassLoader classLoader) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
            }

            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
        return classes;
    }

    private static class CustomClassLoader {
        private enum ClassLoaderType {
            JAR, THREAD
        }
    }

}
