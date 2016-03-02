package org.hrodberaht.inject.extension.cdi.cdiext;

import org.hrodberaht.inject.extension.cdi.inner.FileScanningUtil;
import org.hrodberaht.inject.extension.cdi.inner.SimpleLogger;
import org.hrodberaht.inject.internal.annotation.ReflectionUtils;
import org.hrodberaht.inject.spi.ContainerConfig;
import org.hrodberaht.inject.spi.InjectionRegisterScanInterface;

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

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class CDIExtensions {

    protected enum Phase {AfterBeanDiscovery, BeforeBeanDiscovery}

    protected Map<Phase, List<MethodClassHolder>> phaseMethods = new ConcurrentHashMap<Phase, List<MethodClassHolder>>();

    public CDIExtensions() {
        phaseMethods.put(Phase.BeforeBeanDiscovery, new ArrayList<MethodClassHolder>());
        phaseMethods.put(Phase.AfterBeanDiscovery, new ArrayList<MethodClassHolder>());
        findExtensions();
    }

    public void runAfterBeanDiscovery(InjectionRegisterScanInterface register, ContainerConfig containerConfig) {
        AfterBeanDiscoveryByInject inject = new AfterBeanDiscoveryByInject(register);
        List<MethodClassHolder> methods = phaseMethods.get(Phase.AfterBeanDiscovery);
        for (MethodClassHolder methodClassHolder : methods) {
            try {
                methodClassHolder.getMethod().setAccessible(true);
                Object instance = methodClassHolder.getaClass().newInstance();
                register.getInjectContainer().injectDependencies(instance);
                if (methodClassHolder.getMethod().getParameterTypes().length == 1) {
                    methodClassHolder.getMethod().invoke(instance, inject);
                } else {
                    // TODO: figure out what to do some day
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void runBeforeBeanDiscovery(InjectionRegisterScanInterface register, ContainerConfig containerConfig) {
        BeforeBeanDiscoveryByInject inject = new BeforeBeanDiscoveryByInject();
        List<MethodClassHolder> methods = phaseMethods.get(Phase.BeforeBeanDiscovery);
        for (MethodClassHolder methodClassHolder : methods) {
            try {
                methodClassHolder.getMethod().setAccessible(true);
                Object instance = methodClassHolder.getaClass().newInstance();
                // Not possible to inject dependencies before the container is built
                // register.getInjectContainer().injectDependencies(instance);
                if (methodClassHolder.getMethod().getParameterTypes().length == 1) {
                    methodClassHolder.getMethod().invoke(instance, inject);
                } else {
                    // TODO: figure out what to do some day
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void findExtensions() {
        try {
            String extensionFileName = "javax.enterprise.inject.spi.Extension";
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/" + extensionFileName);
            for (URL resource; resources.hasMoreElements(); ) {
                resource = resources.nextElement();
                String path = resource.getFile();
                SimpleLogger.log("evaluating jar-file = " + path);
                if (FileScanningUtil.isJarFile(resource)) {
                    SimpleLogger.log("findJarFiles fileToLoad = " + path);
                    JarFile jarFile = new JarFile(FileScanningUtil.findJarFile(path));
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
                                SimpleLogger.log("jar aClass error: " + classPath);
                            } catch (NoClassDefFoundError e) {
                                SimpleLogger.log("jar aClass error: " + classPath);
                                throw new RuntimeException("jar aClass error: " + classPath, e);
                            }
                        }
                    }
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(resource.toURI()))));
                    readExtensionsAndRegister(br);
                    br.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readExtensionsAndRegister(BufferedReader br) throws IOException {
        String strLine;
        while ((strLine = br.readLine()) != null) {
            try {
                evaluateMethodAndPutToCache(strLine);
            } catch (Exception e) {
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
            for (int i = 0; i < parameters.length; i++) {
                Class parameter = parameters[i];
                if (parameterAnnotations[0][0].annotationType() == Observes.class) {
                    if (parameter.equals(AfterBeanDiscovery.class)) {
                        this.phaseMethods.get(Phase.AfterBeanDiscovery).add(new MethodClassHolder(aClass, method));
                    }
                    if (parameter.equals(BeforeBeanDiscovery.class)) {
                        this.phaseMethods.get(Phase.BeforeBeanDiscovery).add(new MethodClassHolder(aClass, method));
                    }
                }
            }

        }
    }


}
