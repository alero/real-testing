package test.com.hrodberaht.inject.extension.transaction.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.com.hrodberaht.inject.extension.transaction.JPANewTransactionScopeDisabledUnitT;
import test.com.hrodberaht.inject.extension.transaction.JPANewTransactionScopeUnitT;
import test.com.hrodberaht.inject.extension.transaction.JPATransactionManagerUnitT;
import test.com.hrodberaht.inject.extension.transaction.SimpleServiceWithRunnerUnitT;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-22 18:18:03
 * @version 1.0
 * @since 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        JPATransactionManagerUnitT.class
        , JPANewTransactionScopeUnitT.class
        , JPANewTransactionScopeDisabledUnitT.class
        , SimpleServiceWithRunnerUnitT.class
        // TestJPATransactionManagerPerformance.class
})
public class SuiteJPA {
}
