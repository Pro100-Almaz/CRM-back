package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class DealClosedEvent extends ModuleEvent {
    private final UUID dealId;
    private final UUID userId;
    private final boolean won;
    private final Double amount;
    private final Object dealData;

    public DealClosedEvent(CrmModule module, UUID dealId, UUID userId, boolean won,
                           Double amount, Object dealData) {
        super(module, "DEAL_CLOSED");
        this.dealId = dealId;
        this.userId = userId;
        this.won = won;
        this.amount = amount;
        this.dealData = dealData;
    }
}
