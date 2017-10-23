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

package org.hrodberaht.injection.plugin.junit.cdi;

import org.hrodberaht.injection.internal.annotation.ReflectionUtils;
import org.hrodberaht.injection.register.InjectionRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ApplicationCDIExtensions implements CDIExtensions {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationCDIExtensions.class);

    protected enum Phase {AfterBeanDiscovery, BeforeBeanDiscovery}

    protected Map<Phase, List<MethodClassHolder>> phaseMethods = new ConcurrentHashMap<Phase, List<MethodClassHolder>>();

    public ApplicationCDIExtensions() {
        phaseMethods.put(Phase.BeforeBeanDiscovery, new ArrayList<>());
        phaseMethods.put(Phase.AfterBeanDiscovery, new ArrayList<>());
        findExtensions();
    }

    public void runAfterBeanDiscovery(InjectionRegister register) {
        AfterBeanDiscoveryByInject inject = new AfterBeanDiscoveryByInject(register);
        List<MethodClassHolder> methods = phaseMethods.get(Phase.AfterBeanDiscovery);
        for (MethodClassHolder methodClassHolder : methods) {
            try {
                methodClassHolder.getMethod().setAccessible(true);
                Object instance = methodClassHolder.getaClass().newInstance();
                register.getContainer().injectDependencies(instance);
                if (methodClassHolder.getMethod().getParameterTypes().length == 1) {
                    methodClassHolder.getMethod().invoke(instance, inject);
                } else {
                    // TODO: figure out what to do some day
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new CDIException(e);
            }
        }
    }

    public void runBeforeBeanDiscovery() {
        BeforeBeanDiscoveryByInject inject = new BeforeBeanDiscoveryByInject();
        List<MethodClassHolder> methods = phaseMethods.get(Phase.BeforeBeanDiscovery);
        for (MethodClassHolder methodClassHolder : methods) {
            try {
                methodClassHolder.getMethod().setAccessible(true);
                Object instance = methodClassHolder.getaClass().newInstance();
                // Not possible to injectMethod dependencies before the container is built
                if (methodClassHolder.getMethod().getParameterTypes().length == 1) {
                    methodClassHolder.getMethod().invoke(instance, inject);
                } else {
                    // TODO: figure out what to do some day
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new CDIException(e);
            }
        }
    }

    private void findExtensions() {
        try {
            String extensionFileName = "javax.enterprise.inject.spi.Extension";
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/" + extensionFileName);
            for (URL resource; resources.hasMoreElements(); ) {
                resource = resources.nextElement();
                String path = resource.getFile();
                LOG.info("evaluating jar-file for path: {}", path);
                if (FileScanningUtil.isJarFile(resource)) {
                    LOG.info("findJarFiles fileToLoad = {}", path);
                    try (JarFile jarFile = new JarFile(FileScanningUtil.findJarFile(path))) {
                        handleJarEntries(extensionFileName, jarFile);
                    }
                } else {
                    try (FileInputStream fileInputStream = new FileInputStream(new File(resource.toURI()));
                         BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                    ) {
                        readExtensionsAndRegister(br);
                    }
                }
            }
        } catch (Exception e) {
            throw new CDIException(e);
        }
    }

    private void handleJarEntries(String extensionFileName, JarFile jarFile) throws IOException {
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            String classPath = jarEntry.getName().replaceAll("/", ".");
            if (!jarEntry.isDirectory() && jarEntry.getName().contains(extensionFileName)) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarEntry)));
                    readExtensionsAndRegister(br);
                    br.close();
                } catch (ClassFormatError e) {
                    LOG.debug("jar aClass error: {}", classPath);
                } catch (NoClassDefFoundError e) {
                    throw new CDIException("jar aClass error: " + classPath);
                }
            }
        }
    }

    private void readExtensionsAndRegister(BufferedReader br) throws IOException {
        String strLine;
        while ((strLine = br.readLine()) != null) {
            try {
                evaluateMethodAndPutToCache(strLine);
            } catch (Exception e) {
                LOG.debug("readExtensionsAndRegister - hidden error : {}", e.getMessage());
            }
        }
    }

    private void evaluateMethodAndPutToCache(String strLine) throws ClassNotFoundException {
        String classToLookup = strLine;
        classToLookup = classToLookup.trim();
        Class aClass = Class.forName(classToLookup);
        List<Method> methods = ReflectionUtils.findMethods(aClass);
        for (Method method : methods) {
            Class[] parameters = method.getParameterTypes();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            if (parameterAnnotations.length != 1) {
                if (parameterAnnotations.length == 0) {
                    continue;
                } else if (parameterAnnotations[0].length != 1) {
                    continue;
                }
            }
            LOG.info("adding CDI extension {} - {}", aClass.getName(), method.getName());
            discoveryAnnotation(new MethodClassHolder(aClass, method), parameters, parameterAnnotations[0]);
        }
    }

    private void discoveryAnnotation(MethodClassHolder e, Class[] parameters, Annotation[] parameterAnnotation) {
        for (Class parameter : parameters) {
            if (parameterAnnotation[0].annotationType() == Observes.class) {
                if (parameter.equals(AfterBeanDiscovery.class)) {
                    this.phaseMethods.get(Phase.AfterBeanDiscovery).add(e);
                }
                if (parameter.equals(BeforeBeanDiscovery.class)) {
                    this.phaseMethods.get(Phase.BeforeBeanDiscovery).add(e);
                }
            }
        }
    }


}
