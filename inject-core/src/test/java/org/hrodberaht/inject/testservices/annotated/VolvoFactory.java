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

package org.hrodberaht.inject.testservices.annotated;

import org.hrodberaht.injection.register.InjectionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Robert Work
 * Date: 2010-aug-18
 * Time: 15:31:20
 * To change this template use File | Settings | File Templates.
 */
public class VolvoFactory implements InjectionFactory<Volvo> {
    public Volvo getInstance() {
        Volvo volvo = new Volvo();
        volvo.setInformation("Made from factory");
        return volvo;
    }

    public Class getInstanceType() {
        return Volvo.class;
    }

    public boolean newObjectOnInstance() {
        return true;
    }
}
