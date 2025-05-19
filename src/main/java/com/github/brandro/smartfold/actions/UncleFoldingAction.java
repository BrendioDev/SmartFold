package com.github.brandro.smartfold.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;

import static com.github.brandro.smartfold.OneLineCommentFoldingBuilder.ONE_LINE_COMMENT_PREFIX;


public class UncleFoldingAction extends AnAction {

    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        FoldingModel foldingModel = editor.getFoldingModel();

        foldingModel.runBatchFoldingOperation(() -> {
            FoldRegion[] allRegions = foldingModel.getAllFoldRegions();

            for (FoldRegion region : allRegions) {
                String placeholderText = region.getPlaceholderText().trim();
                if (placeholderText.startsWith("/*") ||
                        placeholderText.startsWith("/**") ||
                        placeholderText.startsWith("//") ||
                        // one line comment built by OneLineCommentFoldingBuilder
                        placeholderText.startsWith(ONE_LINE_COMMENT_PREFIX)) {
                    region.setExpanded(false);
                }
            }
        });
    }

    @Override
    public void update(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        event.getPresentation().setText("Uncle Fold");
        event.getPresentation().setDescription("Fold code at uncle nodes");
    }
}