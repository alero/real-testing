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

package org.hrodberaht.injection.plugin.junit.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ConstantClassLoadedPostContainer {

    private String initData = null;

    @PostConstruct
    public void init() {
        initData = "initialized";
    }

    public String getInitData() {
        return initData;
    }
}
