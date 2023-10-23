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

package org.hrodberaht.injection;

import org.hrodberaht.injection.annotation.AnnotationContainerUnitT;
import org.hrodberaht.injection.annotation.AnnotationContainerUnitTForExternalManagedBeans;
import org.hrodberaht.injection.annotation.AnnotationVariableProviderUnitT;
import org.hrodberaht.injection.annotation.CloneContainerRegistrationUnitT;
import org.hrodberaht.injection.annotation.ContainerScanUnitT;
import org.hrodberaht.injection.annotation.ExtendedModuleUnitT;
import org.hrodberaht.injection.annotation.InjectionRegisterUnitT;
import org.hrodberaht.injection.annotation.ScanContainerRegistrationUnitT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * 2010-maj-29 13:38:13
 * @version 1.0
 * @since 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SimpleContainerUnitT.class,
        AnnotationContainerUnitT.class,
        AnnotationContainerUnitTForExternalManagedBeans.class,
        ContainerScanUnitT.class,
        ScopeThreadUnitT.class,
        AnnotationVariableProviderUnitT.class,
        InstanceCreationUnitT.class,
        CloneContainerRegistrationUnitT.class,
        ScanContainerRegistrationUnitT.class,
        InjectionRegisterUnitT.class,
        ExtendedModuleUnitT.class
})
public class TestSuite {
}
