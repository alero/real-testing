package org.hrodberaht.inject.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by alexbrob on 2016-02-03.
 */
public class PerformanceStatistics {

    private String name;
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end;

    public PerformanceStatistics(String name) {
        this.name = name;
    }

    public void end() {
        this.end = LocalDateTime.now();
    }

    public void markAndPrint(String message) {
        System.out.println(getName() + ":" + message +
                " Time in Milliseconds:" + ChronoUnit.MILLIS.between(start, LocalDateTime.now())
        );
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
