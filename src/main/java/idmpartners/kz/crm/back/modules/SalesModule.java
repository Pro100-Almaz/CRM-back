package idmpartners.kz.crm.back.modules;

import idmpartners.kz.crm.back.abstracts.AbstractCrmModule;
import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.enums.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "crm.modules.sales.enabled", havingValue = "true", matchIfMissing = true)
public class SalesModule extends AbstractCrmModule {

    @Override
    public ModuleType getModuleType() {
        return ModuleType.SALES;
    }

    @Override
    public String getDisplayName() {
        return ModuleType.SALES.getDisplayName();
    }

    @Override
    public List<PermissionType> getRequiredPermissions() {
        return PermissionType.getByModule(ModuleType.SALES);
    }

    @Override
    public List<ActionDefinition> getAvailableActions() {
        return Arrays.asList(
                ActionDefinition.builder()
                        .actionType(ActionType.CREATE)
                        .resourceType(ResourceType.LEADS)
                        .label("Create Lead")
                        .icon("plus")
                        .build(),
                ActionDefinition.builder()
                        .actionType(ActionType.UPDATE)
                        .resourceType(ResourceType.LEADS)
                        .label("Edit Lead")
                        .icon("edit")
                        .build(),
                ActionDefinition.builder()
                        .actionType(ActionType.DELETE)
                        .resourceType(ResourceType.LEADS)
                        .label("Delete Lead")
                        .icon("trash")
                        .requiresConfirmation(true)
                        .build(),
                ActionDefinition.builder()
                        .actionType(ActionType.CONVERT)
                        .resourceType(ResourceType.LEADS)
                        .label("Convert to Deal")
                        .icon("arrow-right")
                        .restrictedToRoles(Set.of(RoleType.SALES_MANAGER, RoleType.SALES_SUPERVISOR))
                        .build()
        );
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected boolean isActionEnabled(ActionDefinition action, UUID userId, UUID resourceId) {
        if (action.getActionType() == ActionType.CONVERT && resourceId != null) {
            return !isLeadConverted(resourceId);
        }
        return true;
    }

    private boolean isLeadConverted(UUID leadId) {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
