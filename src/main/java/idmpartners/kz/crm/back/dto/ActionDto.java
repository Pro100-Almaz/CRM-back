package idmpartners.kz.crm.back.dto;

import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionDto {
    private ActionType actionType;
    private ResourceType resourceType;
    private String label;
    private String icon;
    private boolean enabled;
    private String reason;
    private boolean requiresConfirmation;

    public String getAction() {
        return actionType.getCode();
    }

    public String getResource() {
        return resourceType.getCode();
    }
}
