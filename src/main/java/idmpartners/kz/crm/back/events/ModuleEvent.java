package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Getter
@ToString
public abstract class ModuleEvent {
    private final CrmModule sourceModule;
    private final ModuleType moduleType;
    private final UUID eventId;
    private final long timestamp;
    private final String eventType;
    private final Map<String, Object> metadata;

    protected ModuleEvent(CrmModule sourceModule, String eventType) {
        this.sourceModule = sourceModule;
        this.moduleType = sourceModule.getModuleType();
        this.eventId = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
        this.eventType = eventType;
        this.metadata = new HashMap<>();
    }

    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
}







// Resource-specific events





// Business-specific events for Sales module







// Notification events
