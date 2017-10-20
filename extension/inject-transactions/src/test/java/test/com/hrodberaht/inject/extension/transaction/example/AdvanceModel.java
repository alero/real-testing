package test.com.hrodberaht.inject.extension.transaction.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-10 18:32:03
 * @version 1.0
 * @since 1.0
 */
@Entity
public class AdvanceModel {

    @Id
    private Long id;

    private String name;
    private Date aDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar aCalendar;

    private java.sql.Date sqlDate;
    private java.sql.Time sqlTime;
    private Timestamp sqlTimestamp;


    // Primitives
    private Long longObject;
    private long longPrimitive;
    private Integer intObject;
    private int intPrimitive;
    private Double doubleObject;
    private double doublePrimitive;
    private float floatPrimitive;
    private Float floatObject;
    private Byte byteObject;
    private byte bytePrimitive;


    private BigDecimal bigDecimal;
    private byte[] binaryData;


    private Date aNull = null;

    private Clob aClob;
    private Blob aBlob;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getaDate() {
        return aDate;
    }

    public void setaDate(Date aDate) {
        this.aDate = aDate;
    }

    public java.sql.Date getSqlDate() {
        return sqlDate;
    }

    public void setSqlDate(java.sql.Date sqlDate) {
        this.sqlDate = sqlDate;
    }

    public Time getSqlTime() {
        return sqlTime;
    }

    public void setSqlTime(Time sqlTime) {
        this.sqlTime = sqlTime;
    }

    public Timestamp getSqlTimestamp() {
        return sqlTimestamp;
    }

    public void setSqlTimestamp(Timestamp sqlTimestamp) {
        this.sqlTimestamp = sqlTimestamp;
    }

    public Long getLongObject() {
        return longObject;
    }

    public void setLongObject(Long longObject) {
        this.longObject = longObject;
    }

    public long getLongPrimitive() {
        return longPrimitive;
    }

    public void setLongPrimitive(long longPrimitive) {
        this.longPrimitive = longPrimitive;
    }

    public Integer getIntObject() {
        return intObject;
    }

    public void setIntObject(Integer intObject) {
        this.intObject = intObject;
    }

    public int getIntPrimitive() {
        return intPrimitive;
    }

    public void setIntPrimitive(int intPrimitive) {
        this.intPrimitive = intPrimitive;
    }

    public Double getDoubleObject() {
        return doubleObject;
    }

    public void setDoubleObject(Double doubleObject) {
        this.doubleObject = doubleObject;
    }

    public double getDoublePrimitive() {
        return doublePrimitive;
    }

    public void setDoublePrimitive(double doublePrimitive) {
        this.doublePrimitive = doublePrimitive;
    }

    public float getFloatPrimitive() {
        return floatPrimitive;
    }

    public void setFloatPrimitive(float floatPrimitive) {
        this.floatPrimitive = floatPrimitive;
    }

    public Float getFloatObject() {
        return floatObject;
    }

    public void setFloatObject(Float floatObject) {
        this.floatObject = floatObject;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public Date getaNull() {
        return aNull;
    }

    public void setaNull(Date aNull) {
        this.aNull = aNull;
    }

    public Clob getaClob() {
        return aClob;
    }

    public void setaClob(Clob aClob) {
        this.aClob = aClob;
    }

    public Blob getaBlob() {
        return aBlob;
    }

    public void setaBlob(Blob aBlob) {
        this.aBlob = aBlob;
    }

    public Calendar getaCalendar() {
        return aCalendar;
    }

    public void setaCalendar(Calendar aCalendar) {
        this.aCalendar = aCalendar;
    }

    public byte getBytePrimitive() {
        return bytePrimitive;
    }

    public void setBytePrimitive(byte bytePrimitive) {
        this.bytePrimitive = bytePrimitive;
    }

    public Byte getByteObject() {
        return byteObject;
    }

    public void setByteObject(Byte byteObject) {
        this.byteObject = byteObject;
    }
}
