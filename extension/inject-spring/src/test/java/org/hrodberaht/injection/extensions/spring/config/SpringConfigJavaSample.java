package org.hrodberaht.injection.extensions.spring.config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by robertalexandersson on 4/14/16.
 */
@Configuration
@ComponentScan(basePackages = "org.hrodberaht.injection.extensions.spring.testservices.spring")
public class SpringConfigJavaSample {


    /**
     * Works same as
     * <jee:jndi-lookup id="MyDataSource" jndi-name="jdbc/MyDataSource" expected-type="javax.sql.DataSource" />
     *
     * @return
     */

    @Bean
    @Qualifier("MyDataSource")
    public DataSource dataSource() {
        final JndiDataSourceLookup jndiLookup = new JndiDataSourceLookup();
        return jndiLookup.getDataSource("jdbc/MyDataSource");
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return Mockito.mock(PlatformTransactionManager.class);
    }

}
