package com.github.brendio.smartfold.fold;

import com.github.brendio.smartfold.AbstractFoldingBuilder;
import com.intellij.psi.PsiComment;
import com.jetbrains.python.PyTokenTypes;

public final class PyFoldingBuilder extends AbstractFoldingBuilder {

    @Override
    protected boolean isOneLineComment(PsiComment comment) {
        return comment.getTokenType() == PyTokenTypes.END_OF_LINE_COMMENT;
    }
}


