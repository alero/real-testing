package org.hrodberaht.injection.extensions.junit.datasource;

import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.hrodberaht.injection.extensions.junit.internal.embedded.ResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
    private String[] resourceFiles;
    private Map<File, FileWatcher> filesToWatch;
    private Map<File, FileWatcher> filesToWatchCache;

    private boolean hasChanged = false;

    private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    public FileTimestampResourceWatcher(File timestampHolder, String... resourceFiles) {
        this.timestampHolder = timestampHolder;
        this.resourceFiles = resourceFiles;
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
                Resource resource = patternResolver.getResource(resourceFile);
                if (!resource.getURL().getProtocol().equals("jar")) {
                    File file = resource.getFile();
                    FileWatcher fileWatcher = new FileWatcher(file.getPath(),
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault())
                    );
                    filesToWatch.put(file, fileWatcher);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean compareEquals(Map<File, FileWatcher> filesToWatch, Map<File, FileWatcher> filesToWatchCache) {
        for (Map.Entry<File, FileWatcher> file : filesToWatch.entrySet()) {
            FileWatcher fileWatcher = filesToWatchCache.get(file.getKey());
            if(fileWatcher == null){
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
