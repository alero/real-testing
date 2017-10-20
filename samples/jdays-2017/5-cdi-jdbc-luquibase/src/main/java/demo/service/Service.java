package demo.service;


import com.hrodberaht.inject.extensions.jdbc.Insert;
import com.hrodberaht.inject.extensions.jdbc.InsertOrUpdater;
import com.hrodberaht.inject.extensions.jdbc.JDBCService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class Service {

    @Inject
    Provider<JDBCService> jdbcServiceProvider;

    public void createData(String key, String value) {
        Insert insert = jdbcServiceProvider.get().createInsert("simple");
        insert.field("aName", key);
        insert.field("aValue", value);
        jdbcServiceProvider.get().insert(insert);
    }

    public void changeIt(String key, String value) {
        InsertOrUpdater insertOrUpdater = jdbcServiceProvider.get().createInsertOrUpdate("simple");
        insertOrUpdater.where("aName", key);
        insertOrUpdater.field("aValue", value);
        jdbcServiceProvider.get().insertOrUpdate(insertOrUpdater);
    }

    public String findIt(String key) {
        return jdbcServiceProvider.get().querySingle("select aValue from simple where aName = ?", (resultSet, i) -> resultSet.getString("aValue"), key);

    }

}
