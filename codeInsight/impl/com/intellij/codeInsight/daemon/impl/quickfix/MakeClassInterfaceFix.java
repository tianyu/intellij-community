package com.intellij.codeInsight.daemon.impl.quickfix;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInspection.IntentionQuickFix;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MakeClassInterfaceFix extends IntentionQuickFix {
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.daemon.impl.quickfix.MakeClassInterfaceFix");

  private final PsiClass myClass;
  private final boolean myMakeInterface;

  public MakeClassInterfaceFix(PsiClass aClass, final boolean makeInterface) {
    myClass = aClass;
    myMakeInterface = makeInterface;
  }

  @NotNull
  public String getName() {
    return QuickFixBundle.message(myMakeInterface? "make.class.an.interface.text":"make.interface.an.class.text", myClass.getName());
  }

  @NotNull
  public String getFamilyName() {
    return QuickFixBundle.message("make.class.an.interface.family");
  }

  public boolean isAvailable(@NotNull final Project project, final Editor editor, final PsiFile file) {
    return myClass.isValid() && myClass.getManager().isInProject(myClass);
  }

  public void applyFix(final Project project, final PsiFile file, @Nullable final Editor editor) {
    if (!CodeInsightUtilBase.preparePsiElementForWrite(myClass)) return;
    try {
      final PsiReferenceList extendsList = myMakeInterface? myClass.getExtendsList() : myClass.getImplementsList();
      final PsiReferenceList implementsList = myMakeInterface? myClass.getImplementsList() : myClass.getExtendsList();
      if (extendsList != null) {
        for (PsiJavaCodeReferenceElement referenceElement : extendsList.getReferenceElements()) {
          referenceElement.delete();
        }
        if (implementsList != null) {
          for (PsiJavaCodeReferenceElement referenceElement : implementsList.getReferenceElements()) {
            extendsList.addAfter(referenceElement, null);
            referenceElement.delete();
          }
        }
      }
      convertPsiClass(myClass, myMakeInterface);
      UndoUtil.markPsiFileForUndo(file);
    } catch (IncorrectOperationException e) {
      LOG.error(e);
    }
  }

  private void convertPsiClass(PsiClass aClass, final boolean makeInterface) throws IncorrectOperationException {
    final IElementType lookFor = makeInterface? JavaTokenType.CLASS_KEYWORD : JavaTokenType.INTERFACE_KEYWORD;
    final PsiKeyword replaceWith = JavaPsiFacade.getInstance(myClass.getProject()).getElementFactory().createKeyword(makeInterface? PsiKeyword.INTERFACE : PsiKeyword.CLASS);
    for (PsiElement psiElement : aClass.getChildren()) {
      if (psiElement instanceof PsiKeyword) {
        final PsiKeyword psiKeyword = (PsiKeyword)psiElement;
        if (psiKeyword.getTokenType() == lookFor) {
          psiKeyword.replace(replaceWith);
          break;
        }
      }
    }
  }
}
