package com.github.brendio.smartfold.fold;

import com.github.brendio.smartfold.AbstractFoldingBuilder;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiComment;

public final class JavaFoldingBuilder extends AbstractFoldingBuilder {

    @Override
    protected boolean isOneLineComment(PsiComment comment) {
        return comment.getTokenType() == JavaTokenType.END_OF_LINE_COMMENT;
    }
}

