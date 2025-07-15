package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class NotificationEvent extends ModuleEvent {
    public enum NotificationType {
        INFO, WARNING, ERROR, SUCCESS
    }

    private final NotificationType notificationType;
    private final String title;
    private final String message;
    private final UUID targetUserId;
    private final UUID relatedResourceId;
    private final String actionUrl;

    public NotificationEvent(CrmModule module, NotificationType notificationType, String title,
                             String message, UUID targetUserId, UUID relatedResourceId, String actionUrl) {
        super(module, "NOTIFICATION");
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.targetUserId = targetUserId;
        this.relatedResourceId = relatedResourceId;
        this.actionUrl = actionUrl;
    }

    public static NotificationEvent info(CrmModule module, String title, String message, UUID targetUserId) {
        return new NotificationEvent(module, NotificationType.INFO, title, message, targetUserId, null, null);
    }

    public static NotificationEvent warning(CrmModule module, String title, String message, UUID targetUserId) {
        return new NotificationEvent(module, NotificationType.WARNING, title, message, targetUserId, null, null);
    }

    public static NotificationEvent error(CrmModule module, String title, String message, UUID targetUserId) {
        return new NotificationEvent(module, NotificationType.ERROR, title, message, targetUserId, null, null);
    }

    public static NotificationEvent success(CrmModule module, String title, String message, UUID targetUserId) {
        return new NotificationEvent(module, NotificationType.SUCCESS, title, message, targetUserId, null, null);
    }
}