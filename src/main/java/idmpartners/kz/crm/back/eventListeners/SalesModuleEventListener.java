package idmpartners.kz.crm.back.eventListeners;


import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.events.ActionPerformedEvent;
import idmpartners.kz.crm.back.events.ResourceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesModuleEventListener {

    @EventListener
    public void handleDealsCreated(ResourceCreatedEvent event) {
        if (event.getResourceType() == ResourceType.DEALS) {
            log.info("New deal created, triggering sales follow-up");
        }
    }

    @EventListener
    public void handleMarketingCampaignCompleted(ActionPerformedEvent event) {
        if (event.getSourceModule().getModuleType() == ModuleType.MARKETING &&
                event.getAction().getActionType().name().equals("CAMPAIGN_COMPLETED")) {

            log.info("Marketing campaign completed, updating sales priorities");
        }
    }
}