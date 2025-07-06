package idmpartners.kz.crm.back.interfaces;

import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.PermissionType;

import java.util.List;

public interface CrmModule {
    ModuleType getModuleType();
    String getDisplayName();
    List<PermissionType> getRequiredPermissions();
    List<ActionDefinition> getAvailableActions();
    boolean isEnabled();
    void initialize();
}
