package com.github.brendio.smartfold;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.github.brendio.smartfold.fold.JavaFoldingBuilder.ONE_LINE_COMMENT_PREFIX;


public class SmartFoldingAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        FoldingModel foldingModel = editor.getFoldingModel();

        foldingModel.runBatchFoldingOperation(() -> {
            FoldRegion[] allRegions = foldingModel.getAllFoldRegions();
            var comments = Arrays.stream(allRegions)
                    .filter(region -> {
                        String placeholderText = region.getPlaceholderText().trim();
                        return isComment(placeholderText);
                    }).toList();
            // if all comments are un expand, expand all
            boolean allNotExpand = comments.stream().allMatch(region -> !region.isExpanded());
            for (FoldRegion region : comments) {
                region.setExpanded(allNotExpand ? true : false);
            }
        });
    }

    private static boolean isComment(String placeholderText) {
        return placeholderText.startsWith("/*") ||
                placeholderText.startsWith("/**") ||
                placeholderText.startsWith("//") ||
                // one line comment built by OneLineCommentFoldingBuilder
                placeholderText.startsWith(ONE_LINE_COMMENT_PREFIX);
    }

    @Override
    public void update(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        event.getPresentation().setText("Smart Fold");
        event.getPresentation().setDescription("Fold comments including one line comments");
    }
}