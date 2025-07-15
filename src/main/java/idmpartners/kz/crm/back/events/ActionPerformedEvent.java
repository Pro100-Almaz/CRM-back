package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class ActionPerformedEvent extends ModuleEvent {
    private final ActionDefinition action;
    private final UUID userId;
    private final UUID resourceId;
    private final Object resourceData;
    private final boolean success;
    private final String errorMessage;

    public ActionPerformedEvent(CrmModule module, ActionDefinition action, UUID userId,
                                UUID resourceId, Object resourceData, boolean success, String errorMessage) {
        super(module, "ACTION_PERFORMED");
        this.action = action;
        this.userId = userId;
        this.resourceId = resourceId;
        this.resourceData = resourceData;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static ActionPerformedEvent success(CrmModule module, ActionDefinition action,
                                               UUID userId, UUID resourceId, Object resourceData) {
        return new ActionPerformedEvent(module, action, userId, resourceId, resourceData, true, null);
    }

    public static ActionPerformedEvent failure(CrmModule module, ActionDefinition action,
                                               UUID userId, UUID resourceId, String errorMessage) {
        return new ActionPerformedEvent(module, action, userId, resourceId, null, false, errorMessage);
    }
}