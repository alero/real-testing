package test.org.hrodberaht.inject.extension.ejbunit.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:29:52
 * @version 1.0
 * @since 1.0
 */
@Component(value = "WithAName")
public class NamedSpringServiceImpl implements NamedSpringServiceInterface {

    @Autowired
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
