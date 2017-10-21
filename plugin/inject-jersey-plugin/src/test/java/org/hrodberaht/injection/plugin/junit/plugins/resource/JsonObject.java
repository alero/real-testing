package org.hrodberaht.injection.plugin.junit.plugins.resource;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class JsonObject {

    private Long aLong;
    private Integer aInteger;
    private String aString;
    private LocalDateTime localDateTime;
    private LocalTime localime;
    private Boolean aBoolean;
    private Double aDouble;

    // For the Json Parser
    public JsonObject(){
    }

    public JsonObject(Long aLong, Integer aInteger, String aString, LocalDateTime localDateTime, LocalTime localime, Boolean aBoolean, Double aDouble) {
        this.aLong = aLong;
        this.aInteger = aInteger;
        this.aString = aString;
        this.localDateTime = localDateTime;
        this.localime = localime;
        this.aBoolean = aBoolean;
        this.aDouble = aDouble;
    }


    public Long getaLong() {
        return aLong;
    }

    public Integer getaInteger() {
        return aInteger;
    }

    public String getaString() {
        return aString;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public LocalTime getLocalime() {
        return localime;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public void setaInteger(Integer aInteger) {
        this.aInteger = aInteger;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setLocalime(LocalTime localime) {
        this.localime = localime;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }
}
