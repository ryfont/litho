/*
 * Copyright (c) Facebook, Inc. and its affiliates.
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

package com.facebook.litho.intellij.completion;

import com.intellij.codeInsight.completion.CompletionResult;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.Consumer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Consumer adds custom {@link LookupElement} items to the given {@link #result}. During consumption
 * if it receives same type as in {@link #replacedQualifiedNames} it creates new one, otherwise
 * passes consumed {@link CompletionResult} unchanged.
 *
 * <p>It should be passed before other consumers.
 *
 * @see #addRemainingCompletions(Project)
 */
class ReplacingConsumer implements Consumer<CompletionResult> {
  private final Set<String> replacedQualifiedNames;
  private final CompletionResultSet result;

  ReplacingConsumer(Collection<String> replacedQualifiedNames, CompletionResultSet result) {
    this.replacedQualifiedNames = new HashSet<>(replacedQualifiedNames);
    this.result = result;
  }

  @Override
  public void consume(CompletionResult completionResult) {
    PsiElement psiElement = completionResult.getLookupElement().getPsiElement();
    Optional<String> qualifiedName =
        Optional.ofNullable(psiElement)
            .filter(PsiClass.class::isInstance)
            .map(psiClass -> ((PsiClass) psiClass).getQualifiedName())
            .filter(replacedQualifiedNames::remove);
    if (qualifiedName.isPresent()) {
      result.addElement(SpecLookupElement.create((PsiClass) psiElement));
    } else {
      result.passResult(completionResult);
    }
  }

  /**
   * Adds {@link LookupElement} for any {@link #replacedQualifiedNames} unseen during consumption.
   */
  void addRemainingCompletions(Project project) {
    for (String qualifiedName : replacedQualifiedNames) {
      result.addElement(SpecLookupElement.create(qualifiedName, project));
    }
  }
}
