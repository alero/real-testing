package test.org.hrodberaht.inject.extension.cdi.service2;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-04
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class CDIServiceWithResource {

    @Resource(name = "MyDataSource")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate = null;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void createStuff(long id, String value) {
        jdbcTemplate.update("insert into theTable values (?, ?)", id, value);
    }

    public String findStuff(long id) {
        return jdbcTemplate.queryForObject("select name from theTable where id = ?", String.class, id);
    }
}
