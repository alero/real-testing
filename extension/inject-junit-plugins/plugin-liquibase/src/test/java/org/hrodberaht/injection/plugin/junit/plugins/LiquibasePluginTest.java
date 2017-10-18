package org.hrodberaht.injection.plugin.junit.plugins;

import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCService;
import org.hrodberaht.injection.plugin.datasource.jdbc.JDBCServiceFactory;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.plugin.junit.config.ContainerConfigExample;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

@ContainerContext(ContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
public class LiquibasePluginTest {
    @Resource
    private DataSource dataSource;

    private JDBCService jdbcService;

    @PostConstruct
    public void init(){
        jdbcService = JDBCServiceFactory.of(dataSource);
    }

    @Test
    public void loadFromSampleFile() throws Exception {
        List<String> stringList =
                jdbcService.query("select * from simple", (rs, iteration) -> rs.getString("name"));

        assertEquals(0, stringList.size());

        assertTrue(Paths.get("target/liquibase/main/backup.script").toFile().exists());

        assertTrue(Paths.get("target/liquibase/main/filewatcher.log").toFile().exists());

    }
}