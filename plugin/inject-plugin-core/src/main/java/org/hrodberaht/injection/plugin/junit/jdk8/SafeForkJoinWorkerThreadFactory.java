package org.hrodberaht.injection.plugin.junit.jdk8;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class SafeForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {


    // https://stackoverflow.com/questions/34303094/is-it-not-possible-to-supply-a-thread-facory-or-name-pattern-to-forkjoinpool

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        return new SafeForkJoinWorkerThread(pool);
    }


    private static class SafeForkJoinWorkerThread extends ForkJoinWorkerThread {

        protected SafeForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);
            setContextClassLoader(ForkJoinPool.class.getClassLoader());
        }
    }
}
