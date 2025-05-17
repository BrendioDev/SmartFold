package com.github.brandro.smartfold.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.TextRange


class UncleFoldingAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }


    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val foldingModel = editor.foldingModel
        val document = editor.document

        // Run folding operations in batch mode for better performance
        foldingModel.runBatchFoldingOperation {
            val allRegions = foldingModel.allFoldRegions

            for (region in allRegions) {
                // Get the full text of the region
                val startOffset = region.startOffset
                val endOffset = region.endOffset
                val fullText = document.getText(TextRange(startOffset, endOffset))
                println("Full text: $fullText")

                // Check if the region is a comment
                if (region.placeholderText.trim().startsWith("/*") ||
                    region.placeholderText.trim().startsWith("/**") ||
                    region.placeholderText.trim().startsWith("//")
                ) {
                    region.isExpanded = false
                }
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        event.presentation.isEnabled = editor != null
        event.presentation.text = "Uncle Fold"
        event.presentation.description = "Fold code at uncle nodes"
    }

}