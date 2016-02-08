package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extension.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extension.transaction.junit.InjectionJUnitTestRunner;
import com.hrodberaht.inject.extension.transaction.manager.impl.jpa.StatisticsJPA;
import com.hrodberaht.inject.extension.transaction.manager.internal.TransactionLogging;
import org.hrodberaht.inject.Container;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.JPATransactedApplication;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForTests;
import test.com.hrodberaht.inject.extension.transaction.example.TransactedApplication;

import javax.ejb.TransactionAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 *  <p/>
 *  To run these tests with load time weaving add the weaver to the JRE like this.
 *  -javaagent:C:/Users/Robert/.m2/repository/org/aspectj/aspectjweaver/1.7.1/aspectjweaver-1.7.1.jar
 *   If the path contains a space do it like this
 *  -javaagent:"C:\Users\alexbrob\.m2\repository\org\aspectj\aspectjweaver\1.7.1\aspectjweaver-1.7.1.jar"
 */

@InjectionContainerContext(ModuleContainerForTests.class)
@RunWith(InjectionJUnitTestRunner.class)
public class JPATransactionManagerPerformanceUnitT {

    @BeforeClass
    public static void initClass() {
        TransactionLogging.enableLogging = false;
    }

    @Before
    public void init() {
        StatisticsJPA.setEnabled(true);
    }

    @After
    public void destroy() {
        StatisticsJPA.setEnabled(false);
    }

    @Test(timeout = 30000)
    public void testMultiThreadPerformanceManyTx() {
        performTest();
    }

    @Test(timeout = 30000)
    @TransactionAttribute
    public void testMultiThreadPerformanceOneTx() {
        performTest();
    }

    private void performTest() {
        JPATransactedApplication.performanceCount = new AtomicLong(0L);
        final int threadCount = 500, iterations = 2000;
        long sleepTime = iterations* JPATransactedApplication.performanceSleeptime;
        System.out.println("Estimated sleep-time = "+(sleepTime)+ "ms");
        Date startDate = new Date();

        final Container container = ModuleContainerForTests.container;

        Collection<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(
                    new Thread() {
                        @Override
                        public void run() {
                            runThreadContainerGet(container, iterations);
                        }
                    }
            );
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            waitForIt(thread);
        }

        System.out.println("Begun transactions: "+ StatisticsJPA.getBeginCount());
        System.out.println("Closed transactions: "+ StatisticsJPA.getCloseCount());

        System.out.println("Committed transactions: "+StatisticsJPA.getCommitCount());
        System.out.println("Rollback transactions: "+StatisticsJPA.getRollbackCount());

        System.out.println("Nr of Calls: "+ JPATransactedApplication.performanceCount.longValue());

        Date endDate = new Date();
        long execTime = endDate.getTime()-startDate.getTime();
        long adjustedTime = execTime-sleepTime;
        System.out.println("Transaction overhead time "+(adjustedTime)+"ms " +
                "for "+ JPATransactedApplication.performanceCount.longValue()+ " calls" +
                " creating " + StatisticsJPA.getBeginCount()+ " transactions"
        );

        System.out.println("Transaction/ms "+(StatisticsJPA.getBeginCount()/adjustedTime));
        System.out.println("Calls/ms "+(JPATransactedApplication.performanceCount.longValue()/adjustedTime));
    }

    private void waitForIt(Thread thread) {
        try {
            while (thread.isAlive())
                Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void runThreadContainerGet(Container container, int iterations) {
        for (int i = 0; i < iterations; i++) {
            TransactedApplication application = container.get(TransactedApplication.class);
            application.fakeOperationForPerformanceTest();
        }
    }
}
