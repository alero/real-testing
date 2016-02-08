package org.hrodberaht.inject.extension.tdd.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-05-08
 * Time: 07:32
 * To change this template use File | Settings | File Templates.
 */
public class JarUtil {

    public static List<File> findTheJarFiles(String packageName, ClassLoader classLoader) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        List<File> jarFiles = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String path = resource.getFile();
            SimpleLogger.log("evaluating jar-file = " + path);
            if (isJarFile(resource)) {
                SimpleLogger.log("found valid jar-file = " + path);
                jarFiles.add(new File(findJarFile(path)));
            }
        }
        SimpleLogger.log("found no valid jar-file");
        return jarFiles;
    }

    private static boolean isJarFile(URL resource) {
        String path = resource.getFile();
        return path.contains(".jar!");
    }

    private static String findJarFile(String file) {
        file = file.replaceAll("%20", " ");
        int sizeOfPrefix[] = null;
        if (file.contains("file:")) {
            sizeOfPrefix = new int[]{6, 5};
        } else if (file.startsWith("/")) {
            sizeOfPrefix = new int[]{1, 0};
        } else {
            throw new IllegalAccessError("no valid evaluation of jar path available");
        }
        if (file.contains("//")) {
            return file.substring(sizeOfPrefix[0], file.indexOf("!"));
        } else {      // for unix
            return file.substring(sizeOfPrefix[1], file.indexOf("!"));
        }
    }

}
