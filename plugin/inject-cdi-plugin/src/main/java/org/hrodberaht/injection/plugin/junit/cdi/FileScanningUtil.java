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

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-06-26
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class FileScanningUtil {

    public static boolean isJarFile(URL resource) {
        String path = resource.getFile();
        return path.contains(".jar!");
    }

    public static String findJarFile(String file) {
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
