package org.hrodberaht.injection.extensions.junit.ejb3.service;

import org.hrodberaht.injection.extensions.junit.common.SomeData;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:29:52
 * @version 1.0
 * @since 1.0
 */
@Stateless
public class EJB3InnerServiceImpl extends PersistentBase implements EJB3InnerServiceInterface {

    @Resource(name = "MyDataSource")
    private DataSource dataSource;


    public void doSomething() {
        System.out.print("Hi there Inner");
    }

    public String findSomething(Long id) {
        return "Something Deep";
    }

    public String findSomethingFromDataSource(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            return performFindForConnection(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String findSomethingFromDataSource2(Long id) {
        try {
            Connection connection = typedDataSource.getConnection();
            return performFindForConnection(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String findSomethingFromEntityManager(Long id) {
        SomeData someData = entityManager.find(SomeData.class, id);
        return someData.getName();
    }

    private static AtomicLong id = new AtomicLong(0);

    public SomeData createSomethingForEntityManager(SomeData someData) {
        if (someData.getId() == null) {
            someData.setId(id.incrementAndGet());
        }
        entityManager.persist(someData);
        entityManager.flush();
        return someData;
    }

    private String performFindForConnection(Long id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from the_table where id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        String message = null;
        if (resultSet.next()) {
            message = resultSet.getString("name");
        }
        // This is not proper socket close handling and will leak in case of errors, but this is a simple test
        resultSet.close();
        preparedStatement.close();
        connection.close();
        return message;
    }

    public void updateSomethingInDataSource(Long id, String name) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update the_table set name = ? where id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();
            // This is not proper socket close handling and will leak in case of errors, but this is a simple test
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
