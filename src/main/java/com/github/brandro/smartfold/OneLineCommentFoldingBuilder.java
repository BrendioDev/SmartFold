package com.github.brandro.smartfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class OneLineCommentFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                          @NotNull Document document,
                                                          boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList();
        // Find your single-line comments
        Collection<PsiElement> comments = List.of(PsiTreeUtil.findChildrenOfType(
                root, PsiComment.class
        ).toArray(PsiElement.EMPTY_ARRAY));
        // dqwdwq
        for (PsiElement comment : comments) {
            descriptors.add(
                    new FoldingDescriptor(
                            comment.getNode(),
                            comment.getTextRange()
                    )
            );
        }

        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return true;
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode astNode) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull FoldingDescriptor foldingDescriptor) {
        return super.isCollapsedByDefault(foldingDescriptor);
    }
}
