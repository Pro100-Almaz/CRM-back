package idmpartners.kz.crm.back.classes;

import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionDefinition {
    private ActionType actionType;
    private ResourceType resourceType;
    private String label;
    private String icon;
    private String description;
    private boolean requiresConfirmation;
    private Set<RoleType> restrictedToRoles;

    public String getAction() {
        return actionType.getCode();
    }

    public String getResource() {
        return resourceType.getCode();
    }
}
