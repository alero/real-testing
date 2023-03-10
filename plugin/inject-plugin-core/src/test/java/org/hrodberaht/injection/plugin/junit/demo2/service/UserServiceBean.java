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

package org.hrodberaht.injection.plugin.junit.demo2.service;

import org.hrodberaht.injection.plugin.junit.model.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class UserServiceBean implements UserService {

    Map<String, User> userMap = new HashMap<>();

    @Resource
    private MyResource myTypedResource;

    @Resource(name = "myResource")
    private MyResource myNamedResource;

    @Resource(mappedName = "myMappedResource")
    private MyResource myMappedNamedResource;

    @PostConstruct
    public void init() {
        User user = new User("dude", "wheremycar");
        userMap.put(user.getUserName(), user);
    }

    @Override
    public User find(String userName) {
        return userMap.get(userName);
    }

    @Override
    public boolean loginCompare(String userName, String password) {
        User user = find(userName);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    @Override
    public MyResource getMyTypedResource() {
        return myTypedResource;
    }

    @Override
    public MyResource getMyNamedResource() {
        return myNamedResource;
    }

    @Override
    public MyResource getMyMappedNamedResource() {
        return myMappedNamedResource;
    }
}
