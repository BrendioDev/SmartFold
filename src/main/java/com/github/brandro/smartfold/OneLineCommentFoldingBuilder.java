package com.github.brandro.smartfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.github.brandro.smartfold.Utils.areOnAdjacentLines;

public final class OneLineCommentFoldingBuilder implements FoldingBuilder {

    public static final String ONE_LINE_COMMENT_PREFIX = "/.";

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
            if (current.getTokenType() != JavaTokenType.END_OF_LINE_COMMENT) {
                continue;
            }
            if (previous != null &&
                    previous.getTokenType() == JavaTokenType.END_OF_LINE_COMMENT &&
                    areOnAdjacentLines(current, previous, astNode.getPsi().getProject())
            ) {
                continue;
            }
            if (next != null &&
                    next.getTokenType() == JavaTokenType.END_OF_LINE_COMMENT &&
                    areOnAdjacentLines(current, next, astNode.getPsi().getProject())
            ) {
                continue;
            }

            descriptors.add(
                    new FoldingDescriptor(
                            current.getNode(),
                            current.getTextRange()
                    )
            );
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
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
