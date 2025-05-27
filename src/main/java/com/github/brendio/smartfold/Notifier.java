package com.github.brendio.smartfold;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class Notifier {

    public static void notify(Project project, String content, NotificationType notificationType) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("SmartFold Notification Group")
                .createNotification(content, notificationType)
                .setTitle("SmartFold")
                .notify(project);
    }

}