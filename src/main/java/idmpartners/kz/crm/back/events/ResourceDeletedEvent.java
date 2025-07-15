package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class ResourceDeletedEvent extends ModuleEvent {
    private final ResourceType resourceType;
    private final UUID resourceId;
    private final UUID userId;
    private final Object resourceData;

    public ResourceDeletedEvent(CrmModule module, ResourceType resourceType, UUID resourceId,
                                UUID userId, Object resourceData) {
        super(module, "RESOURCE_DELETED");
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.userId = userId;
        this.resourceData = resourceData;
    }
}