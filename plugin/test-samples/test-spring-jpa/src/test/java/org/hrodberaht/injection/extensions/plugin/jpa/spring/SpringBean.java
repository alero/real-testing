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

package org.hrodberaht.injection.extensions.plugin.jpa.spring;

import org.hrodberaht.injection.extensions.plugin.jpa.domain.UserObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by alexbrob on 2016-03-29.
 */
@Repository
public class SpringBean {


    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;


    public UserObject getUser(String username) {
        return entityManager.find(UserObject.class, username);
    }

    @Transactional
    public void createUser(String username, String password) {
        entityManager.persist(new UserObject(username, password));
    }


}
