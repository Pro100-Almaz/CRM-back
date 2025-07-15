package idmpartners.kz.crm.back.services;

import idmpartners.kz.crm.back.abstracts.AbstractCrmModule;
import idmpartners.kz.crm.back.abstracts.CrmModuleRegistry;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.classes.ActionDefinition;
import idmpartners.kz.crm.back.annotations.RequireModulePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrmModuleService {

    private final CrmModuleRegistry moduleRegistry;

    /**
     * Get available actions for a user in a specific module
     */
    @RequireModulePermission(
            module = ModuleType.SALES,
            resourceType = ResourceType.LEADS,
            actionType = ActionType.READ
    )
    public List<ActionDefinition> getAvailableActions(ModuleType moduleType, UUID userId) {
        return moduleRegistry.getModule(moduleType)
                .map(CrmModule::getAvailableActions)
                .orElse(List.of());
    }

    /**
     * Check if a specific action is available for a user
     */
    public boolean canPerformAction(ModuleType moduleType,
                                    ResourceType resourceType,
                                    ActionType actionType,
                                    UUID userId) {

        // Check if module is enabled
        if (!moduleRegistry.isModuleEnabled(moduleType)) {
            log.debug("Module {} is not enabled", moduleType);
            return false;
        }

        // Check if action exists
        Optional<ActionDefinition> actionOpt = moduleRegistry.getAction(moduleType, resourceType, actionType);
        if (actionOpt.isEmpty()) {
            log.debug("Action {}:{} not found in module {}", resourceType, actionType, moduleType);
            return false;
        }

        ActionDefinition action = actionOpt.get();

        // Get the actual module to check business logic
        Optional<CrmModule> moduleOpt = moduleRegistry.getModule(moduleType);
        if (moduleOpt.isPresent() && moduleOpt.get() instanceof AbstractCrmModule abstractModule) {
            if (!abstractModule.canPerformAction(action, userId, null)) {
                return false;
            }
        }

        // Additional permission checks using your PermissionRepository could go here

        return true;
    }

    /**
     * Get module information for dashboard/UI
     */
    public List<ModuleInfo> getModulesForUser(UUID userId) {
        return moduleRegistry.getEnabledModules().stream()
                .map(module -> ModuleInfo.builder()
                        .moduleType(module.getModuleType())
                        .displayName(module.getDisplayName())
                        .actionCount(module.getAvailableActions().size())
                        .enabled(module.isEnabled())
                        .build())
                .toList();
    }

    /**
     * Get system health information
     */
    public SystemHealth getSystemHealth() {
        CrmModuleRegistry.ModuleRegistryStats stats = moduleRegistry.getStats();
        List<String> issues = moduleRegistry.validateModules();

        return SystemHealth.builder()
                .totalModules(stats.getTotalModules())
                .enabledModules(stats.getEnabledModules())
                .totalActions(stats.getTotalActions())
                .validationIssues(issues)
                .healthy(issues.isEmpty())
                .build();
    }

    // DTOs
    public static class ModuleInfo {
        private final ModuleType moduleType;
        private final String displayName;
        private final int actionCount;
        private final boolean enabled;

        private ModuleInfo(ModuleType moduleType, String displayName, int actionCount, boolean enabled) {
            this.moduleType = moduleType;
            this.displayName = displayName;
            this.actionCount = actionCount;
            this.enabled = enabled;
        }

        public static ModuleInfoBuilder builder() {
            return new ModuleInfoBuilder();
        }

        // Getters
        public ModuleType getModuleType() { return moduleType; }
        public String getDisplayName() { return displayName; }
        public int getActionCount() { return actionCount; }
        public boolean isEnabled() { return enabled; }

        public static class ModuleInfoBuilder {
            private ModuleType moduleType;
            private String displayName;
            private int actionCount;
            private boolean enabled;

            public ModuleInfoBuilder moduleType(ModuleType moduleType) {
                this.moduleType = moduleType;
                return this;
            }

            public ModuleInfoBuilder displayName(String displayName) {
                this.displayName = displayName;
                return this;
            }

            public ModuleInfoBuilder actionCount(int actionCount) {
                this.actionCount = actionCount;
                return this;
            }

            public ModuleInfoBuilder enabled(boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public ModuleInfo build() {
                return new ModuleInfo(moduleType, displayName, actionCount, enabled);
            }
        }
    }

    public static class SystemHealth {
        private final int totalModules;
        private final int enabledModules;
        private final int totalActions;
        private final List<String> validationIssues;
        private final boolean healthy;

        private SystemHealth(int totalModules, int enabledModules, int totalActions,
                             List<String> validationIssues, boolean healthy) {
            this.totalModules = totalModules;
            this.enabledModules = enabledModules;
            this.totalActions = totalActions;
            this.validationIssues = validationIssues;
            this.healthy = healthy;
        }

        public static SystemHealthBuilder builder() {
            return new SystemHealthBuilder();
        }

        // Getters
        public int getTotalModules() { return totalModules; }
        public int getEnabledModules() { return enabledModules; }
        public int getTotalActions() { return totalActions; }
        public List<String> getValidationIssues() { return validationIssues; }
        public boolean isHealthy() { return healthy; }

        public static class SystemHealthBuilder {
            private int totalModules;
            private int enabledModules;
            private int totalActions;
            private List<String> validationIssues;
            private boolean healthy;

            public SystemHealthBuilder totalModules(int totalModules) {
                this.totalModules = totalModules;
                return this;
            }

            public SystemHealthBuilder enabledModules(int enabledModules) {
                this.enabledModules = enabledModules;
                return this;
            }

            public SystemHealthBuilder totalActions(int totalActions) {
                this.totalActions = totalActions;
                return this;
            }

            public SystemHealthBuilder validationIssues(List<String> validationIssues) {
                this.validationIssues = validationIssues;
                return this;
            }

            public SystemHealthBuilder healthy(boolean healthy) {
                this.healthy = healthy;
                return this;
            }

            public SystemHealth build() {
                return new SystemHealth(totalModules, enabledModules, totalActions, validationIssues, healthy);
            }
        }
    }
}