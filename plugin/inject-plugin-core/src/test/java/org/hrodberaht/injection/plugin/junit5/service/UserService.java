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

package org.hrodberaht.injection.plugin.junit5.service;

import org.hrodberaht.injection.plugin.junit.model.User;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 * 2011-05-08 01:53
 * @created 1.0
 * @since 1.0
 */


public interface UserService {

    User find(String userName);

    boolean loginCompare(String userName, String password);

    MyResource getMyTypedResource();

    MyResource getMyNamedResource();

    MyResource getMyMappedNamedResource();

}
