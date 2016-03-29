package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionJUnitTestRunner;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.Logging;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForTests;
import test.com.hrodberaht.inject.extension.transaction.example.Person;
import test.com.hrodberaht.inject.extension.transaction.example.TransactedApplication;
import test.com.hrodberaht.inject.extension.transaction.util.StubUtil;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        To run these tests with load time weaving add the weaver to the JRE like this.
 *        -javaagent:C:/Users/Robert/.m2/repository/org/aspectj/aspectjweaver/1.7.1/aspectjweaver-1.7.1.jar
 *        If the path contains a space do it like this
 *        -javaagent:"C:\Users\Robert Work\.m2\repository\org\aspectj\aspectjweaver\1.7.1\aspectjweaver-1.7.1.jar"
 */

@InjectionContainerContext(value = ModuleContainerForTests.class, disableRequiresNewTransaction = true)
@RunWith(InjectionJUnitTestRunner.class)
@TransactionAttribute
public class JPANewTransactionScopeDisabledUnitT {

    @Inject
    private TransactedApplication application;

    @Inject
    private TransactionManager transactionManager;

    @BeforeClass
    public static void initClass() {
        TransactionLogging.enableLogging = true;
    }

    @AfterClass
    public static void destroy() {
        TransactionLogging.enableLogging = false;

        /*

        TODO: REDO this as the static context and the "thread aware" container will
        always fail in strange ways

        InjectContainer container = ModuleContainerForTests.container;
        TransactedApplication application = container.get(TransactedApplication.class);
        Collection<Person> persons = application.findAllPersons();

        assertEquals(0, persons.size());
        */

    }


    @Test
    public void testSingleTransactionWithOpenMainTx2() {

        Person person = StubUtil.createPerson();
        application.createPersonNewTx(person);
        // Rollback the transaction, will not rollback the req new (as it should be committed)
        transactionManager.rollback();


        Person foundPerson = application.findPerson(person.getId());
        assertNull(foundPerson);


        assertFalse(transactionManager.isActive());

    }

    @Test
    public void testSingleTransaction2() {

        Person person = StubUtil.createPerson();
        Person foundPerson = application.depthyTransactionsNewTx(person);

        assertEquals(foundPerson.getName(), person.getName());

        assertTrue(transactionManager.isActive());

    }

    @Test
    public void testSingleTransactionWithErrorAndLogging2() {

        Person person = StubUtil.createPerson();
        Logging log = StubUtil.createLogg("A log message");
        // This code can not work without REQUIRES_NEW and is just a proof of how the restrictions must be handled.

        try {
            Person foundPerson = application.complexTransactionsNewTx(person, log);
            assertEquals("Expected error", foundPerson.getName());
        } catch (Exception e) {
            assertEquals("Transaction is marked for rollback only", e.getMessage());
            assertFalse(transactionManager.isActive());
        }         
    }

}
