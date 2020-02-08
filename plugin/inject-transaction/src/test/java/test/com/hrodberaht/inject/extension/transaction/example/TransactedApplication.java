package test.com.hrodberaht.inject.extension.transaction.example;

import java.util.Collection;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public interface TransactedApplication {

    Person findPerson(Long id);

    Person findPersonReqNew(Long id);

    Person findPersonNative(Long id);

    Person findPersonNativeNoJoin(Long id);

    Collection<Person> findAllPersons();

    Person somethingNonTransactional(Long id);

    void createPerson(Person person);

    void createPersonNewTx(Person person);

    void createPersonMandatory(Person person);

    // Mixes create and find
    Person depthyTransactions(Person person);

    Person depthyTransactionsMandatory(Person person);

    Person depthyTransactionsNewTx(Person person);

    Person depthyTransactionsNotSupported(Person person);

    void fakeOperationForPerformanceTest();

    Person complexTransactionsNewTx(Person person, Logging log);

    void createLog(Logging log);

    Logging getLog(Long id);

    void deletePerson(Person person);

    void clearLogs();
}
