package idmpartners.kz.crm.back.abstracts;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.classes.ActionDefinition;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CrmModuleRegistry {

    private final ApplicationContext applicationContext;
    private final Map<ModuleType, CrmModule> moduleMap = new ConcurrentHashMap<>();
    private final Map<ModuleType, List<ActionDefinition>> moduleActions = new ConcurrentHashMap<>();
    private final Map<String, ActionDefinition> actionIndex = new ConcurrentHashMap<>();

    @Autowired
    public CrmModuleRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initializeRegistry() {
        log.info("Initializing CRM Module Registry...");

        Collection<CrmModule> modules = applicationContext.getBeansOfType(CrmModule.class).values();

        for (CrmModule module : modules) {
            if (module.isEnabled()) {
                registerModule(module);
            } else {
                log.debug("Module {} is disabled, skipping registration", module.getModuleType());
            }
        }

        log.info("CRM Module Registry initialized with {} modules", moduleMap.size());
    }

    private void registerModule(CrmModule module) {
        ModuleType moduleType = module.getModuleType();

        if (moduleMap.containsKey(moduleType)) {
            log.warn("Module {} already registered, skipping duplicate", moduleType);
            return;
        }

        try {
            module.initialize();

            moduleMap.put(moduleType, module);

            List<ActionDefinition> actions = module.getAvailableActions();
            moduleActions.put(moduleType, actions);

            for (ActionDefinition action : actions) {
                String actionKey = buildActionKey(moduleType, action.getResourceType(), action.getActionType());
                actionIndex.put(actionKey, action);
            }

            log.info("Successfully registered module: {} with {} actions",
                    module.getDisplayName(), actions.size());

        } catch (Exception e) {
            log.error("Failed to register module {}: {}", moduleType, e.getMessage(), e);
            throw new RuntimeException("Module registration failed for " + moduleType, e);
        }
    }

    /**
     * Get a module by its type
     */
    public Optional<CrmModule> getModule(ModuleType moduleType) {
        return Optional.ofNullable(moduleMap.get(moduleType));
    }

    /**
     * Get all registered modules
     */
    public Collection<CrmModule> getAllModules() {
        return Collections.unmodifiableCollection(moduleMap.values());
    }

    /**
     * Get all enabled modules
     */
    public List<CrmModule> getEnabledModules() {
        return moduleMap.values().stream()
                .filter(CrmModule::isEnabled)
                .collect(Collectors.toList());
    }

    /**
     * Get modules by resource type
     */
    public List<CrmModule> getModulesByResourceType(ResourceType resourceType) {
        return moduleMap.values().stream()
                .filter(module -> module.getAvailableActions().stream()
                        .anyMatch(action -> action.getResourceType() == resourceType))
                .collect(Collectors.toList());
    }

    /**
     * Get all actions for a specific module
     */
    public List<ActionDefinition> getModuleActions(ModuleType moduleType) {
        return moduleActions.getOrDefault(moduleType, Collections.emptyList());
    }

    /**
     * Get a specific action definition
     */
    public Optional<ActionDefinition> getAction(ModuleType moduleType,
                                                ResourceType resourceType,
                                                ActionType actionType) {
        String actionKey = buildActionKey(moduleType, resourceType, actionType);
        return Optional.ofNullable(actionIndex.get(actionKey));
    }

    /**
     * Get all actions across all modules
     */
    public List<ActionDefinition> getAllActions() {
        return moduleActions.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Get actions by resource type across all modules
     */
    public List<ActionDefinition> getActionsByResourceType(ResourceType resourceType) {
        return moduleActions.values().stream()
                .flatMap(List::stream)
                .filter(action -> action.getResourceType() == resourceType)
                .collect(Collectors.toList());
    }

    /**
     * Get actions by action type across all modules
     */
    public List<ActionDefinition> getActionsByActionType(ActionType actionType) {
        return moduleActions.values().stream()
                .flatMap(List::stream)
                .filter(action -> action.getActionType() == actionType)
                .collect(Collectors.toList());
    }

    /**
     * Check if a module is registered and enabled
     */
    public boolean isModuleEnabled(ModuleType moduleType) {
        return moduleMap.containsKey(moduleType) && moduleMap.get(moduleType).isEnabled();
    }

    /**
     * Check if a specific action is available
     */
    public boolean isActionAvailable(ModuleType moduleType,
                                     ResourceType resourceType,
                                     ActionType actionType) {
        String actionKey = buildActionKey(moduleType, resourceType, actionType);
        return actionIndex.containsKey(actionKey);
    }

    /**
     * Get module statistics
     */
    public ModuleRegistryStats getStats() {
        return ModuleRegistryStats.builder()
                .totalModules(moduleMap.size())
                .enabledModules((int) moduleMap.values().stream().filter(CrmModule::isEnabled).count())
                .totalActions(actionIndex.size())
                .moduleBreakdown(moduleMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().name(),
                                entry -> entry.getValue().getAvailableActions().size())))
                .build();
    }

    /**
     * Refresh a specific module (useful for dynamic reconfiguration)
     */
    public void refreshModule(ModuleType moduleType) {
        CrmModule module = moduleMap.get(moduleType);
        if (module != null) {
            log.info("Refreshing module: {}", moduleType);

            // Clear cached data
            moduleActions.remove(moduleType);
            actionIndex.entrySet().removeIf(entry -> entry.getKey().startsWith(moduleType.name()));

            // Re-register
            registerModule(module);
        }
    }

    /**
     * Validate module configuration
     */
    public List<String> validateModules() {
        List<String> issues = new ArrayList<>();

        for (Map.Entry<ModuleType, CrmModule> entry : moduleMap.entrySet()) {
            ModuleType moduleType = entry.getKey();
            CrmModule module = entry.getValue();

            if (module.getModuleType() != moduleType) {
                issues.add(String.format("Module type mismatch for %s: expected %s, got %s",
                        moduleType, moduleType, module.getModuleType()));
            }

            List<ActionDefinition> actions = module.getAvailableActions();
            if (actions == null || actions.isEmpty()) {
                issues.add(String.format("Module %s has no actions defined", moduleType));
            } else {
                for (ActionDefinition action : actions) {
                    if (action.getActionType() == null || action.getResourceType() == null) {
                        issues.add(String.format("Module %s has invalid action definition: %s",
                                moduleType, action));
                    }
                }
            }
        }

        return issues;
    }

    private String buildActionKey(ModuleType moduleType, ResourceType resourceType, ActionType actionType) {
        return String.format("%s:%s:%s", moduleType.name(), resourceType.name(), actionType.name());
    }

    public static class ModuleRegistryStats {
        private final int totalModules;
        private final int enabledModules;
        private final int totalActions;
        private final Map<String, Integer> moduleBreakdown;

        private ModuleRegistryStats(int totalModules, int enabledModules, int totalActions,
                                    Map<String, Integer> moduleBreakdown) {
            this.totalModules = totalModules;
            this.enabledModules = enabledModules;
            this.totalActions = totalActions;
            this.moduleBreakdown = moduleBreakdown;
        }

        public static ModuleRegistryStatsBuilder builder() {
            return new ModuleRegistryStatsBuilder();
        }

        public int getTotalModules() { return totalModules; }
        public int getEnabledModules() { return enabledModules; }
        public int getTotalActions() { return totalActions; }
        public Map<String, Integer> getModuleBreakdown() { return moduleBreakdown; }

        public static class ModuleRegistryStatsBuilder {
            private int totalModules;
            private int enabledModules;
            private int totalActions;
            private Map<String, Integer> moduleBreakdown;

            public ModuleRegistryStatsBuilder totalModules(int totalModules) {
                this.totalModules = totalModules;
                return this;
            }

            public ModuleRegistryStatsBuilder enabledModules(int enabledModules) {
                this.enabledModules = enabledModules;
                return this;
            }

            public ModuleRegistryStatsBuilder totalActions(int totalActions) {
                this.totalActions = totalActions;
                return this;
            }

            public ModuleRegistryStatsBuilder moduleBreakdown(Map<String, Integer> moduleBreakdown) {
                this.moduleBreakdown = moduleBreakdown;
                return this;
            }

            public ModuleRegistryStats build() {
                return new ModuleRegistryStats(totalModules, enabledModules, totalActions, moduleBreakdown);
            }
        }
    }
}