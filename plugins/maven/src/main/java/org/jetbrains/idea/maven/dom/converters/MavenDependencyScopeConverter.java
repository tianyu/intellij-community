/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.idea.maven.dom.converters;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomManager;
import org.jetbrains.idea.maven.dom.model.MavenDomDependencyManagement;
import org.jetbrains.idea.maven.utils.MavenConstants;

import java.util.Arrays;
import java.util.List;

public class MavenDependencyScopeConverter extends MavenConstantListConverter {
  private static final List<String> MANAGING_DEPENDENCY_VALUES = Arrays
    .asList(MavenConstants.SCOPE_COMPILE,
            MavenConstants.SCOPE_PROVIDEED,
            MavenConstants.SCOPE_RUNTIME,
            MavenConstants.SCOPE_TEST,
            MavenConstants.SCOPE_SYSTEM,
            MavenConstants.SCOPE_IMPORT);

  private static final List<String> DEPENDENCY_VALUES = Arrays
    .asList(MavenConstants.SCOPE_COMPILE,
            MavenConstants.SCOPE_PROVIDEED,
            MavenConstants.SCOPE_RUNTIME,
            MavenConstants.SCOPE_TEST,
            MavenConstants.SCOPE_SYSTEM);

  public MavenDependencyScopeConverter() {
    super(true);
  }

  protected List<String> getValues(ConvertContext context) {
    boolean isDependencyManagement = context.getInvocationElement().getParentOfType(MavenDomDependencyManagement.class, false) != null;

   return isDependencyManagement ? MANAGING_DEPENDENCY_VALUES : DEPENDENCY_VALUES;
  }
}
