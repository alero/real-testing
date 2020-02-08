package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.AdvanceModel;
import test.com.hrodberaht.inject.extension.transaction.example.JDBCHelperApplication;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForJDBCHelperTests;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-06 18:33:12
 * @version 1.0
 * @since 1.0
 * <p/>
 * To run these tests with load time weaving add the weaver to the JRE like this.
 * -javaagent:C:/Users/Robert/.m2/repository/org/aspectj/aspectjweaver/1.7.1/aspectjweaver-1.7.1.jar
 * If the path contains a space do it like this
 * -javaagent:"C:\Users\alexbrob\.m2\repository\org\aspectj\aspectjweaver\1.7.1\aspectjweaver-1.7.1.jar"
 */
@InjectionContainerContext(ModuleContainerForJDBCHelperTests.class)
@RunWith(InjectionJUnitTestRunner.class)
@TransactionAttribute
public class JDBCHelperClassesUnitT {


    @Inject
    private JDBCHelperApplication application;

    @Test
    public void testCreateDataModel() throws ParseException {
        AdvanceModel advanceModel = createAdvancedModel();
        application.createAdvancedModel(advanceModel);

        AdvanceModel foundAdvanceModel = application.findAdvancedModel(advanceModel.getId());

        assertEquals(advanceModel.getId(), foundAdvanceModel.getId());
        assertEquals(advanceModel.getName(), foundAdvanceModel.getName());
        assertEquals(advanceModel.getaCalendar().getTime(), foundAdvanceModel.getaCalendar().getTime());
        assertEquals(cleanDate(advanceModel.getaDate(), "yyyy-MM-dd"), foundAdvanceModel.getaDate());

        assertEquals(cleanDate(advanceModel.getSqlDate(), "yyyy-MM-dd").getTime()
                , foundAdvanceModel.getSqlDate().getTime());

        assertEquals(cleanDate(advanceModel.getSqlTime(), "HH-mm-ss").getTime(),
                foundAdvanceModel.getSqlTime().getTime());

        assertEquals(advanceModel.getSqlTimestamp().getTime(), foundAdvanceModel.getSqlTimestamp().getTime());

        assertEquals(advanceModel.getaBlob(), foundAdvanceModel.getaBlob());
        assertEquals(advanceModel.getaClob(), foundAdvanceModel.getaClob());
        assertEquals(advanceModel.getaNull(), foundAdvanceModel.getaNull());
        assertEquals(advanceModel.getBigDecimal(), foundAdvanceModel.getBigDecimal());
        assertTrue(Arrays.equals(advanceModel.getBinaryData(), foundAdvanceModel.getBinaryData()));

        assertEquals(advanceModel.getDoubleObject(), foundAdvanceModel.getDoubleObject());
        assertEquals(advanceModel.getDoublePrimitive(), foundAdvanceModel.getDoublePrimitive(), 0);

        assertEquals(advanceModel.getFloatObject(), foundAdvanceModel.getFloatObject());
        assertEquals(advanceModel.getFloatPrimitive(), foundAdvanceModel.getFloatPrimitive(), 0);

        assertEquals(advanceModel.getLongObject(), foundAdvanceModel.getLongObject());
        assertEquals(advanceModel.getLongPrimitive(), foundAdvanceModel.getLongPrimitive());

        assertEquals(advanceModel.getIntObject(), foundAdvanceModel.getIntObject());
        assertEquals(advanceModel.getIntPrimitive(), foundAdvanceModel.getIntPrimitive());

        assertEquals(advanceModel.getByteObject(), foundAdvanceModel.getByteObject());
        assertEquals(advanceModel.getBytePrimitive(), foundAdvanceModel.getBytePrimitive());


        assertNotNull(advanceModel.getaDate());
        assertNotNull(advanceModel.getSqlDate());
        assertNotNull(advanceModel.getSqlTime());
        assertNotNull(advanceModel.getSqlTimestamp());
        assertNotNull(advanceModel.getId());
        assertNotNull(advanceModel.getName());
        assertNotNull(advanceModel.getBigDecimal());
        assertNotNull(advanceModel.getBinaryData());
        assertNotNull(advanceModel.getDoubleObject());
        assertNotNull(advanceModel.getFloatObject());
        assertNotNull(advanceModel.getLongObject());
        assertNotNull(advanceModel.getIntObject());
        assertNotNull(advanceModel.getByteObject());

        // This does not work as nothing is created
        // assertNotNull(advanceModel.getaBlob());
        // assertNotNull(advanceModel.getaClob());
    }

    @Test
    public void testUpdateDataModel() throws ParseException {
        AdvanceModel advanceModel = createAdvancedModel();
        application.createAdvancedModel(advanceModel);
        advanceModel.setName("Testing update");
        application.updateAdvancedModel(advanceModel);

        AdvanceModel foundAdvanceModel = application.findAdvancedModel(advanceModel.getId());
        assertEquals(advanceModel.getName(), foundAdvanceModel.getName());
    }

    private AdvanceModel createAdvancedModel() {
        AdvanceModel advanceModel = new AdvanceModel();
        advanceModel.setId(1L);
        advanceModel.setName("Hi there");
        advanceModel.setaDate(new Date());
        advanceModel.setaCalendar(Calendar.getInstance());
        advanceModel.setSqlDate(new java.sql.Date(new Date().getTime()));
        advanceModel.setSqlTime(new java.sql.Time(new Date().getTime()));
        advanceModel.setSqlTimestamp(new Timestamp(new Date().getTime()));

        advanceModel.setIntObject(12);
        advanceModel.setIntPrimitive(13);
        advanceModel.setDoubleObject(12D);
        advanceModel.setDoublePrimitive(13D);
        advanceModel.setLongObject(12L);
        advanceModel.setLongPrimitive(13L);
        advanceModel.setFloatObject(12F);
        advanceModel.setFloatPrimitive(13F);

        advanceModel.setByteObject((byte) 4);
        advanceModel.setBytePrimitive((byte) 5);

        advanceModel.setBigDecimal(new BigDecimal("12.00"));
        advanceModel.setBinaryData(new byte[]{1, 2, 4, 5});

        return advanceModel;
    }

    private Date cleanDate(Date date, String s) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(s);
        return dateFormat.parse(dateFormat.format(date));
    }


}
