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

package org.hrodberaht.injection.extensions.cdi.example.service;

import javax.ejb.EJB;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class ExampleInterfaceImplementation implements ExampleInterface {

    @EJB
    private AnotherInterface anotherInterface;

    public String getSomething() {
        return "something";
    }

    public String getSomethingElseLikeWhat() {
        return anotherInterface.what();
    }
}
