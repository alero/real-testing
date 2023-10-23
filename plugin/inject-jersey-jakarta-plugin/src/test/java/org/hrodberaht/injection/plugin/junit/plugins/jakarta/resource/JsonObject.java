/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.plugins.jakarta.resource;

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
    public JsonObject() {
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

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public Integer getaInteger() {
        return aInteger;
    }

    public void setaInteger(Integer aInteger) {
        this.aInteger = aInteger;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalTime getLocalime() {
        return localime;
    }

    public void setLocalime(LocalTime localime) {
        this.localime = localime;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }
}
