package test.com.hrodberaht.inject.extension.transaction.example;

import com.hrodberaht.inject.extensions.jdbc.Insert;
import com.hrodberaht.inject.extensions.jdbc.InsertOrUpdater;
import com.hrodberaht.inject.extensions.jdbc.JDBCService;
import com.hrodberaht.inject.extensions.jdbc.RowIterator;
import com.hrodberaht.inject.extensions.jdbc.SQLDateUtil;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-sep-16 20:07:43
 * @version 1.0
 * @since 1.0
 */
public class JDBCHelperApplication {


    @Inject
    private JDBCService jdbcService;

    public void createAdvancedModel(AdvanceModel advanceModel) {
        Insert insert = jdbcService.createInsert("advanceModel");
        insert.field("id", advanceModel.getId());
        updateAllFields(advanceModel, insert);
        jdbcService.insert(insert);
    }


    public void updateAdvancedModel(AdvanceModel advanceModel) {
        InsertOrUpdater updater = jdbcService.createInsertOrUpdate("advanceModel");
        updater.where("id", advanceModel.getId());
        updateAllFields(advanceModel, updater);
        jdbcService.insertOrUpdate(updater);
    }

    public AdvanceModel findAdvancedModel(Long id) {
        String sql = "select * from AdvanceModel where id = ?";
        return jdbcService.querySingle(sql, new AdvanceModelIterator(), id);
    }


    private void updateAllFields(AdvanceModel advanceModel, Insert insert) {
        insert.field("name", advanceModel.getName());
        insert.field("aBlob", advanceModel.getaBlob());
        insert.field("aClob", advanceModel.getaClob());
        insert.field("aNull", advanceModel.getaNull());
        insert.field("bigDecimal", advanceModel.getBigDecimal());
        insert.field("binaryData", advanceModel.getBinaryData());
        insert.field("doubleObject", advanceModel.getDoubleObject());
        insert.field("doublePrimitive", advanceModel.getDoublePrimitive());
        insert.field("floatObject", advanceModel.getFloatObject());
        insert.field("floatPrimitive", advanceModel.getFloatPrimitive());
        insert.field("intObject", advanceModel.getIntObject());
        insert.field("intPrimitive", advanceModel.getIntPrimitive());
        insert.field("longObject", advanceModel.getLongObject());
        insert.field("longPrimitive", advanceModel.getLongPrimitive());
        insert.field("byteObject", advanceModel.getByteObject());
        insert.field("bytePrimitive", advanceModel.getBytePrimitive());
        insert.field("aDate", advanceModel.getaDate());
        insert.field("aCalendar", advanceModel.getaCalendar());
        insert.field("sqlDate", advanceModel.getSqlDate());
        insert.field("sqlTime", advanceModel.getSqlTime());
        insert.field("sqlTimestamp", advanceModel.getSqlTimestamp());
    }

    private static class AdvanceModelIterator implements RowIterator<AdvanceModel> {

        public AdvanceModel iterate(ResultSet rs, int iteration) throws SQLException {
            AdvanceModel advanceModel = new AdvanceModel();
            advanceModel.setId(rs.getLong("id"));
            advanceModel.setName(rs.getString("name"));
            advanceModel.setaDate(SQLDateUtil.getDate(rs.getDate("aDate")));
            advanceModel.setaCalendar(SQLDateUtil.getCalendar(rs.getTimestamp("aCalendar")));
            advanceModel.setSqlDate(rs.getDate("sqlDate"));
            advanceModel.setSqlTime(rs.getTime("sqlTime"));
            advanceModel.setSqlTimestamp(rs.getTimestamp("sqlTimestamp"));
            advanceModel.setBigDecimal(rs.getBigDecimal("bigDecimal"));
            advanceModel.setBinaryData(rs.getBytes("binaryData"));

            advanceModel.setIntObject(rs.getInt("intObject"));
            advanceModel.setIntPrimitive(rs.getInt("intPrimitive"));

            advanceModel.setDoubleObject(rs.getDouble("doubleObject"));
            advanceModel.setDoublePrimitive(rs.getDouble("doublePrimitive"));

            advanceModel.setLongObject(rs.getLong("longObject"));
            advanceModel.setLongPrimitive(rs.getLong("longPrimitive"));

            advanceModel.setFloatObject(rs.getFloat("floatObject"));
            advanceModel.setFloatPrimitive(rs.getFloat("floatPrimitive"));

            advanceModel.setByteObject(rs.getByte("byteObject"));
            advanceModel.setBytePrimitive(rs.getByte("bytePrimitive"));

            return advanceModel;
        }
    }

}
