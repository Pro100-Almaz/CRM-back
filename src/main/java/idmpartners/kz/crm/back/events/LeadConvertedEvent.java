package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class LeadConvertedEvent extends ModuleEvent {
    private final UUID leadId;
    private final UUID dealId;
    private final UUID userId;
    private final Object leadData;
    private final Object dealData;

    public LeadConvertedEvent(CrmModule module, UUID leadId, UUID dealId, UUID userId,
                              Object leadData, Object dealData) {
        super(module, "LEAD_CONVERTED");
        this.leadId = leadId;
        this.dealId = dealId;
        this.userId = userId;
        this.leadData = leadData;
        this.dealData = dealData;
    }
}