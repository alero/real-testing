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

package org.hrodberaht.injection.plugin.junit.jpa;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;

public class JPATestUtil {

    @Inject
    private EntityManagerHolder entityManager;

    public void flushAndClear() {
        Collection<EntityManager> entityManagers =
                entityManager.getEntityManagers();
        for (EntityManager entityManager : entityManagers) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}
