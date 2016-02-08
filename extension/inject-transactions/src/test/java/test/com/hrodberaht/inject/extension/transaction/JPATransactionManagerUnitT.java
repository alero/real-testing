package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extension.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extension.transaction.junit.InjectionJUnitTestRunner;
import com.hrodberaht.inject.extension.transaction.junit.TransactionDisabled;
import com.hrodberaht.inject.extension.transaction.manager.impl.jpa.TransactionManagerJPA;
import com.hrodberaht.inject.extension.transaction.manager.internal.TransactionHandlingError;
import com.hrodberaht.inject.extension.transaction.manager.internal.TransactionLogging;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForTests;
import test.com.hrodberaht.inject.extension.transaction.example.Person;
import test.com.hrodberaht.inject.extension.transaction.example.TransactedApplication;
import test.com.hrodberaht.inject.extension.transaction.util.StubUtil;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-06 18:33:12
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        To run these tests with load time weaving add the weaver to the JRE like this.
 *        -javaagent:C:/Users/Robert/.m2/repository/org/aspectj/aspectjweaver/1.7.1/aspectjweaver-1.7.1.jar
 *        If the path contains a space do it like this
 *        -javaagent:"C:\Users\Robert Work\.m2\repository\org\aspectj\aspectjweaver\1.7.1\aspectjweaver-1.7.1.jar"
 */
@InjectionContainerContext(ModuleContainerForTests.class)
@RunWith(InjectionJUnitTestRunner.class)
@TransactionAttribute
public class JPATransactionManagerUnitT {

    @Inject
    private TransactedApplication application;

    @Inject
    private TransactionManagerJPA transactionManager;

    @BeforeClass
    public static void init() {
        TransactionLogging.enableLogging = true;
    }

    @AfterClass
    public static void destroy() {
        TransactionLogging.enableLogging = false;

    }

    @Test
    public void testCreateManager() {

        Person person = StubUtil.createPerson();
        application.createPerson(person);

        Person foundPerson = application.findPerson(person.getId());
        assertEquals(foundPerson.getName(), person.getName());

    }


    @Test
    public void testCreateManagerInOneTransaction() {

        Person person = StubUtil.createPerson();

        Person foundPerson = application.depthyTransactions(person);

        assertEquals(foundPerson.getName(), person.getName());

    }

    @Test
    @TransactionDisabled
    public void testCreateManagerMandatoryFail() {
        System.out.println(Thread.currentThread().getContextClassLoader().getClass().getName());
        Person person = StubUtil.createPerson();
        try {
            application.createPersonMandatory(person);
            assertEquals("Do not reach this", null);
        } catch (TransactionHandlingError e) {
            assertEquals("has no active transaction", e.getMessage());
        }
    }

    @Test
    public void testCreateManagerInOneTransactionMandatory() {


        Person person = StubUtil.createPerson();
        Person foundPerson = application.depthyTransactionsMandatory(person);

        assertEquals(foundPerson.getName(), person.getName());

    }

    @Test
    public void testCreateManagerInOneTransactionRequiresNew() {

        Person person = StubUtil.createPerson();
        Person foundPerson = application.depthyTransactionsNewTx(person);

        assertEquals(foundPerson.getName(), person.getName());

    }

    @Test(expected = TransactionHandlingError.class)
    public void testCreateManagerInOneTransactionNotSupported() {

        Person person = StubUtil.createPerson();
        application.depthyTransactionsNotSupported(person);

    }

    @Test(expected = TransactionHandlingError.class)
    @TransactionDisabled
    public void testCreateManagerNotSupported() {

        Person person = StubUtil.createPerson();
        person.setName("Dude");
        application.createPerson(person);

        Person foundPerson = application.somethingNonTransactional(person.getId());

        assertEquals(foundPerson.getName(), person.getName());

    }

    @Test
    public void testJDBCNativeJPASupport() {

        Person person = StubUtil.createPerson();
        person.setName("Dude");
        application.createPerson(person);
        transactionManager.getNativeManager().flush();
        
        Person foundPerson = application.findPersonNative(person.getId());

        assertEquals(foundPerson.getName(), person.getName());

    }

    @Test
    @TransactionDisabled
    public void testJDBCNativeJPASupportRestartTransaction() {

        Person person = StubUtil.createPerson();
        person.setName("Dude");
        application.createPerson(person);
        // Connection will be commited/closed here.
        assertFalse(transactionManager.isActive());

        // A new connection must be created by the native vendor (JPA to JDBC)
        Person foundPerson = application.findPersonNative(person.getId());

        assertEquals(foundPerson.getName(), person.getName());

        application.deletePerson(person);

        foundPerson = application.findPersonNative(person.getId());

        assertEquals(null, foundPerson);
    }

    @Test
    @Ignore
    public void testJDBCNativeJPANoJoinTransaction() {

        Person person = StubUtil.createPerson();
        person.setName("Dude");
        application.createPerson(person);
        // Connection will be commited/closed here.
        assertTrue(transactionManager.isActive());

        // A new connection is forced by the native vendor (JPA to JDBC)
        Person foundPerson = application.findPersonNativeNoJoin(person.getId());

        assertEquals(null, foundPerson);

    }


}
