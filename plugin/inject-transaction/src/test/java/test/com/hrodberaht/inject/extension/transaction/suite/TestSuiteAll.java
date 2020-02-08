package test.com.hrodberaht.inject.extension.transaction.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.com.hrodberaht.inject.extension.transaction.JDBCTransactionManagerUnitT;

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
        SuiteJPA.class
        , JDBCTransactionManagerUnitT.class
})
public class TestSuiteAll {
}
