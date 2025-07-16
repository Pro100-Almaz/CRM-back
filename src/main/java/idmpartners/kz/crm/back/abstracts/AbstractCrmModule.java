package idmpartners.kz.crm.back.abstracts;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
public abstract class AbstractCrmModule implements CrmModule {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private volatile boolean initialized = false;

    @Override
    public final void initialize() {
        if (initialized) {
            log.debug("Module {} already initialized", getModuleType());
            return;
        }

        try {
            log.info("Initializing module: {}", getDisplayName());

            // Validate module configuration
            validateConfiguration();

            // Perform module-specific initialization
            doInitialize();

            initialized = true;

            // Publish module initialized event
            publishEvent(new ModuleInitializedEvent(this, getVersion(), getAvailableActions().size()));

            log.info("Module {} initialized successfully", getDisplayName());

        } catch (Exception e) {
            log.error("Failed to initialize module {}: {}", getDisplayName(), e.getMessage(), e);
            throw new ModuleInitializationException("Failed to initialize module " + getDisplayName(), e);
        }
    }

    /**
     * Template method for module-specific initialization
     */
    protected abstract void doInitialize();

    /**
     * Validate module configuration
     */
    protected void validateConfiguration() {
        // Default validation - can be overridden
        if (getModuleType() == null) {
            throw new IllegalStateException("Module type cannot be null");
        }

        if (getDisplayName() == null || getDisplayName().trim().isEmpty()) {
            throw new IllegalStateException("Display name cannot be null or empty");
        }

        List<ActionDefinition> actions = getAvailableActions();
        if (actions == null) {
            throw new IllegalStateException("Available actions cannot be null");
        }

        // Validate each action
        for (ActionDefinition action : actions) {
            if (action.getActionType() == null || action.getResourceType() == null) {
                throw new IllegalStateException("Action definition is invalid: " + action);
            }
        }
    }

    /**
     * Get module dependencies (override if needed)
     */
    public Set<ModuleType> getDependencies() {
        return Set.of();
    }

    /**
     * Get module version (override if needed)
     */
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * Check if user can perform action on specific resource
     */
    public boolean canPerformAction(ActionDefinition action, UUID userId, UUID resourceId) {
        if (!isEnabled()) {
            return false;
        }

        if (!isActionEnabled(action, userId, resourceId)) {
            return false;
        }

        return true;
    }

    /**
     * Template method for action availability check
     */
    protected boolean isActionEnabled(ActionDefinition action, UUID userId, UUID resourceId) {
        return true; // Default implementation
    }

    /**
     * Publish module events
     */
    protected void publishEvent(ModuleEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }

    /**
     * Helper methods for common event publishing
     */
    protected void publishActionPerformed(ActionDefinition action, UUID userId, UUID resourceId, Object resourceData) {
        publishEvent(ActionPerformedEvent.success(this, action, userId, resourceId, resourceData));
    }

    protected void publishActionFailed(ActionDefinition action, UUID userId, UUID resourceId, String errorMessage) {
        publishEvent(ActionPerformedEvent.failure(this, action, userId, resourceId, errorMessage));
    }

    protected void publishResourceCreated(ResourceType resourceType, UUID resourceId, UUID userId, Object resourceData) {
        publishEvent(new ResourceCreatedEvent(this, resourceType, resourceId, userId, resourceData));
    }

    protected void publishResourceUpdated(ResourceType resourceType, UUID resourceId, UUID userId, Object oldData, Object newData) {
        publishEvent(new ResourceUpdatedEvent(this, resourceType, resourceId, userId, oldData, newData));
    }

    protected void publishResourceDeleted(ResourceType resourceType, UUID resourceId, UUID userId, Object resourceData) {
        publishEvent(new ResourceDeletedEvent(this, resourceType, resourceId, userId, resourceData));
    }

    protected void publishPermissionDenied(ActionDefinition action, UUID userId, UUID resourceId, String reason) {
        publishEvent(new PermissionDeniedEvent(this, action, userId, resourceId, reason));
    }

    protected void publishNotification(NotificationEvent.NotificationType type, String title, String message, UUID targetUserId) {
        publishEvent(new NotificationEvent(this, type, title, message, targetUserId, null, null));
    }

    /**
     * Check if module is properly initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    // Exception classes
    public static class ModuleInitializationException extends RuntimeException {
        public ModuleInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}