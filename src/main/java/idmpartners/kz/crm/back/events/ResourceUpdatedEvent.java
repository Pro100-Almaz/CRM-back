package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class ResourceUpdatedEvent extends ModuleEvent {
    private final ResourceType resourceType;
    private final UUID resourceId;
    private final UUID userId;
    private final Object oldData;
    private final Object newData;

    public ResourceUpdatedEvent(CrmModule module, ResourceType resourceType, UUID resourceId,
                                UUID userId, Object oldData, Object newData) {
        super(module, "RESOURCE_UPDATED");
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.userId = userId;
        this.oldData = oldData;
        this.newData = newData;
    }
}
