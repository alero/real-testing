package org.hrodberaht.injection.extensions.junit.datasource;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWatcher implements Serializable {

    private String fileName;
    private LocalDateTime timestamp;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public FileWatcher(String fileName, LocalDateTime timestamp) {
        this.fileName = fileName;
        this.timestamp = timestamp;
    }


    public String writeString() {
        return fileName + ";" + timestamp.format(dateTimeFormatter);
    }

    public static FileWatcher readString(String line) {
        String[] strings = line.split(";");
        return new FileWatcher(strings[0], LocalDateTime.parse(strings[1], dateTimeFormatter));
    }

    public File getFile() {
        return new File(fileName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileWatcher that = (FileWatcher) o;

        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
