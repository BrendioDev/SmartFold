package com.github.brandro.smartfold;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class Utils {

    public static boolean areOnAdjacentLines(PsiElement e1,
                                             PsiElement e2,
                                             Project project) {
        PsiFile psiFile = e1.getContainingFile();
        Document document = PsiDocumentManager.getInstance(project)
                .getDocument(psiFile);
        if (document == null) return false;
        int line1 = document.getLineNumber(e1.getTextOffset());
        int line2 = document.getLineNumber(e2.getTextOffset());

        if (Math.abs(line1 - line2) == 1) {
            return lineOffset(e1, document, line1) == lineOffset(e2, document, line2);
        } else {
            return false;
        }
    }

    private static int lineOffset(PsiElement e, Document document, int line) {
        int elementOffset = e.getTextRange().getStartOffset();
        int lineStartOffset = document.getLineStartOffset(line);
        return elementOffset - lineStartOffset;
    }

//    public static boolean isElementAtLineStart(PsiElement element, Project project) {
//        PsiFile psiFile = element.getContainingFile();
//        Document document = PsiDocumentManager.getInstance(project)
//                .getDocument(psiFile);
//        int elementOffset = element.getTextRange().getStartOffset();
//        assert document != null;
//        int lineNumber = document.getLineNumber(elementOffset);
//        int lineStartOffset = document.getLineStartOffset(lineNumber);
//
//        return elementOffset == lineStartOffset;
//    }
}
