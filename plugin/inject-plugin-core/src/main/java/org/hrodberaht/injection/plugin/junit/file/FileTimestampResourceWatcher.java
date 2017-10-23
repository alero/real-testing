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

package org.hrodberaht.injection.plugin.junit.file;

import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FileTimestampResourceWatcher implements ResourceWatcher {

    private static final Logger LOG = LoggerFactory.getLogger(FileTimestampResourceWatcher.class);

    private File timestampHolder;
    private Map<File, FileWatcher> filesToWatch;
    private Map<File, FileWatcher> filesToWatchCache;

    private boolean hasChanged = false;

    public FileTimestampResourceWatcher(File timestampHolder, String... resourceFiles) {
        this.timestampHolder = timestampHolder;
        if (resourceFiles != null) {
            filesToWatchCache = new HashMap<>(resourceFiles.length);
            prepareFileToWatch(resourceFiles);
            if (filesToWatch != null && filesToWatch.size() > 0) {
                computeSyncStatusFromWatchers(timestampHolder);
            }

        }

    }

    private void computeSyncStatusFromWatchers(File timestampHolder) {
        if (!timestampHolder.exists()) {
            LOG.debug("First time syncing");
            writeLatestStatus();
            hasChanged = true;
        } else {
            try (Stream<String> stream = Files.lines(Paths.get(timestampHolder.getPath()))) {
                stream.forEach(line -> {
                            FileWatcher fileWatcher = FileWatcher.readString(line);
                            filesToWatchCache.put(fileWatcher.getFile(), fileWatcher);
                        }
                );
                if (!compareEquals(filesToWatch, filesToWatchCache)) {
                    LOG.debug("Syncing on changed sourcefile");
                    hasChanged = true;
                    writeLatestStatus();
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LOG.debug("Not syncing");
            hasChanged = false;
        }
    }

    private void prepareFileToWatch(String[] resourceFiles) {
        filesToWatch = new HashMap<>(resourceFiles.length);

        for (String resourceFile : resourceFiles) {
            try {
                // Resource resource = patternResolver.getResource(resourceFile);
                if (resourceFile.startsWith("classpath:")) {
                    URL resource = FileTimestampResourceWatcher.class.getResource(cleanUpClasspath(resourceFile));
                    if (!resource.getProtocol().equals("jar")) {
                        File file = new File(resource.toURI());
                        FileWatcher fileWatcher = new FileWatcher(file.getPath(),
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault())
                        );
                        filesToWatch.put(file, fileWatcher);
                    }
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String cleanUpClasspath(String resourceFile) {
        String classPath = resourceFile.substring(10, resourceFile.length());
        return classPath.startsWith("/") ? classPath : "/" + classPath;
    }

    private boolean compareEquals(Map<File, FileWatcher> filesToWatch, Map<File, FileWatcher> filesToWatchCache) {
        for (Map.Entry<File, FileWatcher> file : filesToWatch.entrySet()) {
            FileWatcher fileWatcher = filesToWatchCache.get(file.getKey());
            if (fileWatcher == null) {
                return false;
            }
            if (!fileWatcher.equals(file.getValue())) {
                return false;
            }
        }

        return true;
    }

    private void writeLatestStatus() {
        Path path = timestampHolder.toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Map.Entry<File, FileWatcher> file : filesToWatch.entrySet()) {
                writer.write(file.getValue().writeString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

}
