package test.com.hrodberaht.inject.extension.transaction.example;

import com.hrodberaht.inject.extension.jdbc.Insert;
import com.hrodberaht.inject.extension.jdbc.JDBCService;
import com.hrodberaht.inject.extension.jdbc.RowIterator;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:29:58
 * @version 1.0
 * @since 1.0
 */
public class JDBCTransactedApplication implements TransactedApplication {


    @Inject
    private JDBCService jdbcService;


    public void createPerson(Person person) {
        createPerson(person, false);
    }

    @TransactionAttribute
    public void createPerson(Person person, boolean fakeException) {
        if (fakeException) {
            throw new RuntimeException("Bad call, rollbacktime");
        }
        createPersonJDBC(person);

    }


    @TransactionAttribute
    public void deletePerson(Person person) {
        String sql = "delete from Person where id = ?";
        jdbcService.execute(sql, person.getId());
    }

    @TransactionAttribute
    public void clearLogs() {
        String sql = "delete from Logging";
        jdbcService.execute(sql);
    }

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public Person findPerson(Long id) {
        return findPersonJDBC(id);
    }



    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public Person findPersonReqNew(Long id) {
        return findPersonJDBC(id);
    }

    public Person findPersonNative(Long id) {
        return findPersonJDBC(id);
    }

    public Person findPersonNativeNoJoin(Long id) {
        return findPersonJDBC(id);
    }

    @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
    public Collection<Person> findAllPersons() {
        String sql = "select * from Person";
        return jdbcService.query(sql, new PersonIterator());      
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void createLog(Logging log) {
        createLogJDBC(log);
    }

    public Logging getLog(Long id) {
        return findLogJDBC(id);
    }

    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    public Person somethingNonTransactional(Long id) {
        return findPersonJDBC(id);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void createPersonNewTx(Person person) {
        createPersonJDBC(person);
    }

    @TransactionAttribute(value = TransactionAttributeType.MANDATORY)
    public void createPersonMandatory(Person person) {
        createPersonJDBC(person);
    }

    @TransactionAttribute
    public Person depthyTransactions(Person person) {
        // This only works for AspectJ, most AOP frameworks need "lookup" the service again.
        createPerson(person);
        return findPerson(person.getId());
    }

    @TransactionAttribute
    public Person depthyTransactionsMandatory(Person person) {
        // This only works for AspectJ, most AOP frameworks need "lookup" the service again.
        createPersonMandatory(person);
        return findPerson(person.getId());
    }

    @TransactionAttribute
    public Person depthyTransactionsNewTx(Person person) {
        // This only works for AspectJ, most AOP frameworks need "lookup" the service again.
        createPersonNewTx(person);
        return findPerson(person.getId());
    }


    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public Person complexTransactionsNewTx(Person person, Logging log) {
        // This only works for AspectJ, most AOP frameworks need "lookup" the service again.
        try {
            createPerson(person, true);
        } catch (Exception e) {
            createLog(log);
            // A classic log and ignore, should be shot for using this as demo ...
        }
        return findPerson(person.getId());
    }

    @TransactionAttribute
    public Person depthyTransactionsNotSupported(Person person) {
        // This only works for AspectJ, most AOP frameworks need "lookup" the service again.
        createPerson(person);
        return somethingNonTransactional(person.getId());
    }


    public static AtomicLong performanceCount = new AtomicLong(0L);
    public static int performanceSleeptime = 2;

    @TransactionAttribute
    public void fakeOperationForPerformanceTest() {
        try {
            
            // A classic DB call takes about 5 ms
            Thread.sleep(performanceSleeptime);
            performanceCount.incrementAndGet();
        } catch (Exception e) {

        }
    }

    private Person findPersonJDBC(Long id) {
        String sql = "select * from Person where id = ?";
        return jdbcService.querySingle(sql, new PersonIterator(), id);
    }
    private void createPersonJDBC(Person person) {
        Insert insert = jdbcService.createInsert("Person");
        insert.field("id",person.getId());
        insert.field("name", person.getName());

        errorHandleInsertUpdate(insert);
    }

    private Logging findLogJDBC(Long id) {
        String sql = "select * from Logging where id = "+id;
        return jdbcService.querySingle(sql, new LogIterator(), id);
    }
    private void createLogJDBC(Logging log) {
        Insert insert = jdbcService.createInsert("Logging");
        insert.field("id",log.getId());
        insert.field("message", log.getMessage());

        errorHandleInsertUpdate(insert);
    }

    private void errorHandleInsertUpdate(Insert insert) {
        int insertReturn = jdbcService.insert(insert);
        if(insertReturn == 0){
            throw new RuntimeException("Insert Failed");
        }
    }


    private class PersonIterator implements RowIterator<Person> {


        public Person iterate(ResultSet rs, int iteration) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong("id"));
            person.setName(rs.getString("name"));
            return person;
        }
    }

    private class LogIterator implements RowIterator<Logging> {


        public Logging iterate(ResultSet rs, int iteration) throws SQLException {
            Logging person = new Logging();
            person.setId(rs.getLong("id"));
            person.setMessage(rs.getString("message"));
            return person;
        }
    }

}
