package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionJUnitTestRunner;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionDisabled;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionHandlingError;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForJDBCTests;
import test.com.hrodberaht.inject.extension.transaction.example.Person;
import test.com.hrodberaht.inject.extension.transaction.example.TransactedApplication;
import test.com.hrodberaht.inject.extension.transaction.util.StubUtil;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-06 18:33:12
 * @version 1.0
 * @since 1.0
 * <p/>
 * To run these tests with load time weaving add the weaver to the JRE like this.
 * -javaagent:"C:\Users\alexbrob\.m2\repository\org\aspectj\aspectjweaver\1.8.3\aspectjweaver-1.8.3.jar"
 * -javaagent:${settings.localRepository}\org\aspectj\aspectjweaver\1.8.3\aspectjweaver-1.8.3.jar
 */
@InjectionContainerContext(ModuleContainerForJDBCTests.class)
@RunWith(InjectionJUnitTestRunner.class)
@TransactionAttribute
public class JDBCTransactionManagerUnitT {


    @Inject
    private TransactedApplication application;


    @BeforeClass
    public static void init() {
        TransactionLogging.enableLogging = true;
    }

    @AfterClass
    // @TransactionAttribute
    public static void destroy() {
        /*InjectContainer container = ModuleContainerForJDBCTests.container;
        TransactedApplication application = container.get(TransactedApplication.class);
        Collection<Person> collection = application.findAllPersons();

        assertEquals(2, collection.size());

        // Verify that all values are cleared automatically from the test being transactional and calling rollback.
        assertNotNull(collection);
        for (Person person : collection) {
            application.deletePerson(person);
        }
        System.out.println("TestJDBCTransactionManager performed successfully");
        collection = application.findAllPersons();
        assertEquals(0, collection.size());
        */
    }

    @Test
    @TransactionDisabled
    public void testFindPersonsWithOnlySupported() {

        Collection<Person> collection = application.findAllPersons();

        // Verify that all values are cleared automatically from the test being transactional and calling rollback.
        assertEquals(0, collection.size());

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

    @Test(expected = TransactionHandlingError.class)
    @TransactionDisabled
    public void testCreateManagerMandatoryFail() {
        System.out.println(Thread.currentThread().getContextClassLoader().getClass().getName());
        Person person = StubUtil.createPerson();
        application.createPersonMandatory(person);
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

    @Test
    public void testCreateManagerInOneTransactionNotSupported() {

        try {
            Person person = StubUtil.createPerson();
            application.depthyTransactionsNotSupported(person);
            assertEquals("not", "here");
        } catch (TransactionHandlingError e) {
            assertNotNull(e);
        }
    }

    @Test(expected = TransactionHandlingError.class)
    @TransactionDisabled
    public void testCreateManagerNotSupported() {

        Person person = new Person();
        person.setId(55L);
        person.setName("Dude");
        application.createPerson(person);

        Person foundPerson = application.somethingNonTransactional(person.getId());

        assertEquals(foundPerson.getName(), person.getName());

    }


}
