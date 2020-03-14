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

package org.hrodberaht.injection.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by alexbrob on 2016-02-03.
 */
public class PerformanceStatistics {

    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end;
    private String name;

    public PerformanceStatistics(String name) {
        this.name = name;
    }

    public void end() {
        this.end = LocalDateTime.now();
    }

    public void markAndPrint(String message) {
        System.out.println(getName() + ":" + message +
                " Time in Milliseconds:" + ChronoUnit.MILLIS.between(start, LocalDateTime.now())
        );
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
