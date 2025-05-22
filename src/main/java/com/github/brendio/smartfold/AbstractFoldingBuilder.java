package com.github.brendio.smartfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFoldingBuilder implements FoldingBuilder {

    public static final String ONE_LINE_COMMENT_PREFIX = "/.";

    protected abstract boolean isOneLineComment(PsiComment comment);

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        PsiElement[] comments = PsiTreeUtil.findChildrenOfType(
                astNode.getPsi(), PsiComment.class
        ).toArray(PsiElement.EMPTY_ARRAY);
        for (int i = 0; i < comments.length; i++) {
            PsiComment current = (PsiComment) comments[i];
            PsiComment previous = i > 0 ? (PsiComment) comments[i - 1] : null;
            PsiComment next = i < comments.length - 1 ? (PsiComment) comments[i + 1] : null;
            if (!isOneLineComment(current))
                continue;
            if (previous != null &&
                    isOneLineComment(previous) &&
                    areOnAdjacentLines(current, previous, astNode.getPsi().getProject()))
                continue;
            if (next != null &&
                    isOneLineComment(next) &&
                    areOnAdjacentLines(current, next, astNode.getPsi().getProject()))
                continue;

            descriptors.add(
                    new FoldingDescriptor(
                            current.getNode(),
                            current.getTextRange()
                    )
            );
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

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



    @Override
    public String getPlaceholderText(@NotNull ASTNode astNode) {
        return ONE_LINE_COMMENT_PREFIX;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }


    @Override
    public boolean isDumbAware() {
        return true;
    }
}
