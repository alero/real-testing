package com.hrodberaht.inject.extension.transaction.manager.impl.jdbc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-21 20:29:06
 * @version 1.0
 * @since 1.0
 */
public class StatisticsJDBC {

    private static AtomicLong beginCount = new AtomicLong(0L);
    private static AtomicLong commitCount = new AtomicLong(0L);
    private static AtomicLong rollbackCount = new AtomicLong(0L);
    private static AtomicLong closeCount = new AtomicLong(0L);

    private static boolean enabled = false;


    public static void setEnabled(boolean enabled) {
        StatisticsJDBC.enabled = enabled;
        // Reset
        if (!StatisticsJDBC.enabled) {
            beginCount = new AtomicLong(0L);
            commitCount = new AtomicLong(0L);
            rollbackCount = new AtomicLong(0L);
            closeCount = new AtomicLong(0L);
        }
    }

    public static Long getBeginCount() {
        return beginCount.longValue();
    }

    public static Long getCommitCount() {
        return commitCount.longValue();
    }

    public static Long getRollbackCount() {
        return rollbackCount.longValue();
    }

    public static Long getCloseCount() {
        return closeCount.longValue();
    }


    public static void addBeginCount() {
        beginCount.incrementAndGet();
    }

    public static void addCommitCount() {
        commitCount.incrementAndGet();
    }

    public static void addRollbackCount() {
        rollbackCount.incrementAndGet();
    }

    public static void addCloseCount() {
        closeCount.incrementAndGet();
    }
}
