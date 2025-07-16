package idmpartners.kz.crm.back.eventListeners;

import idmpartners.kz.crm.back.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleEventListener {

    @EventListener
    public void handleLeadConverted(LeadConvertedEvent event) {
        log.info("Lead {} converted to deal {} by user {}",
                event.getLeadId(), event.getDealId(), event.getUserId());

    }

    @EventListener
    public void handleDealClosed(DealClosedEvent event) {
        log.info("Deal {} closed by user {} - Won: {}, Amount: {}",
                event.getDealId(), event.getUserId(), event.isWon(), event.getAmount());

        if (event.isWon()) {
        } else {
        }
    }

    @TransactionalEventListener
    public void handleActionPerformed(ActionPerformedEvent event) {
        log.debug("Action {} performed by user {} on resource {} in module {}",
                event.getAction().getActionType(),
                event.getUserId(),
                event.getResourceId(),
                event.getModuleType());

    }

    @EventListener
    public void handlePermissionDenied(PermissionDeniedEvent event) {
        log.warn("Permission denied for user {} attempting {} on resource {} in module {}: {}",
                event.getUserId(),
                event.getAction().getActionType(),
                event.getResourceId(),
                event.getModuleType(),
                event.getReason());
    }

    @EventListener
    public void handleNotification(NotificationEvent event) {
        log.info("Notification {} for user {}: {}",
                event.getNotificationType(),
                event.getTargetUserId(),
                event.getMessage());
    }

    @EventListener
    public void handleResourceCreated(ResourceCreatedEvent event) {
        log.info("Resource {} created in module {} by user {}",
                event.getResourceType(),
                event.getModuleType(),
                event.getUserId());
    }

    @EventListener
    public void handleResourceUpdated(ResourceUpdatedEvent event) {
        log.info("Resource {} updated in module {} by user {}",
                event.getResourceType(),
                event.getModuleType(),
                event.getUserId());
    }

    @EventListener
    public void handleModuleInitialized(ModuleInitializedEvent event) {
        log.info("Module {} initialized with {} actions",
                event.getModuleType(),
                event.getActionCount());
    }

}
