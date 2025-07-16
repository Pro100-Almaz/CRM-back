package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class PermissionDeniedEvent extends ModuleEvent {
    private final ActionDefinition action;
    private final UUID userId;
    private final UUID resourceId;
    private final String reason;

    public PermissionDeniedEvent(CrmModule module, ActionDefinition action, UUID userId,
                                 UUID resourceId, String reason) {
        super(module, "PERMISSION_DENIED");
        this.action = action;
        this.userId = userId;
        this.resourceId = resourceId;
        this.reason = reason;
    }
}