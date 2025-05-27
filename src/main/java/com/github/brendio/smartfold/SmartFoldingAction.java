package com.github.brendio.smartfold;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.github.brendio.smartfold.AbstractFoldingBuilder.ONE_LINE_COMMENT_PREFIX;


public class SmartFoldingAction extends AnAction {
    private static final Logger LOG = Logger.getInstance(SmartFoldingAction.class);

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            performed(event);
        } catch (Exception e) {
            Notifier.notify(event.getProject(), e.getMessage(), NotificationType.WARNING);
            LOG.error("Error while performing SmartFoldingAction", e);
        }
    }

    private void performed(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        assert editor != null;
        FoldingModel foldingModel = editor.getFoldingModel();

        foldingModel.runBatchFoldingOperation(() -> {
            FoldRegion[] allRegions = foldingModel.getAllFoldRegions();
            var comments = Arrays.stream(allRegions)
                    .filter(region1 -> isComment(region1, editor)).toList();
            // if all comments are un expand, expand all
            boolean allNotExpand = comments.stream().allMatch(region -> !region.isExpanded());
            for (FoldRegion region : comments) {
                region.setExpanded(allNotExpand ? true : false);
            }
        });
    }

    private static boolean isComment(FoldRegion region, Editor editor) {
        String placeholderText = region.getPlaceholderText();
        if (placeholderText.startsWith("/*") ||
                placeholderText.startsWith("/**") ||
                placeholderText.startsWith("//") ||
                // one line comment built by OneLineCommentFoldingBuilder
                placeholderText.startsWith(ONE_LINE_COMMENT_PREFIX))
            return true;
        else {
            // check whether its py comment that
            return editor.getDocument().getText(new TextRange(region.getStartOffset(), region.getEndOffset()))
                    .startsWith("#");
        }
    }

    @Override
    public void update(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        event.getPresentation().setText("Smart Fold");
        event.getPresentation().setDescription("Fold comments including one line comments");
    }
}