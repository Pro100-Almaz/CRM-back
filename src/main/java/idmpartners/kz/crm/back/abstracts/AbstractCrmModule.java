package idmpartners.kz.crm.back.abstracts;

import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.dto.ActionDto;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.modules.ModuleRegistry;
import idmpartners.kz.crm.back.services.PermissionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public abstract class AbstractCrmModule implements CrmModule {

    @Autowired
    protected PermissionService permissionService;

    @Autowired
    protected ModuleRegistry moduleRegistry;


    @PostConstruct
    public void registerModule() {
        moduleRegistry.registerModule(this);
    }

    public List<ActionDto> getAvailableActionsForUser(UUID userId, ResourceType resourceType, UUID resourceId) {
        List<ActionDto> availableActions = new ArrayList<>();

        for (ActionDefinition action : getAvailableActions()) {
            if (action.getResourceType() == resourceType &&
                    permissionService.hasPermission(userId, resourceType, action.getActionType(), resourceId)) {

                availableActions.add(ActionDto.builder()
                        .actionType(action.getActionType())
                        .resourceType(action.getResourceType())
                        .label(action.getLabel())
                        .icon(action.getIcon())
                        .enabled(isActionEnabled(action, userId, resourceId))
                        .build());
            }
        }

        return availableActions;
    }

    protected abstract boolean isActionEnabled(ActionDefinition action, UUID userId, UUID resourceId);
}
