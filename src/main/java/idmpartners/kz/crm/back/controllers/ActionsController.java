package idmpartners.kz.crm.back.controllers;

import idmpartners.kz.crm.back.dto.ActionDto;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.interfaces.CrmModule;
import idmpartners.kz.crm.back.modules.ModuleRegistry;
import idmpartners.kz.crm.back.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/actions")
@PreAuthorize("isAuthenticated()")
public class ActionsController {

    @Autowired
    private ModuleRegistry moduleRegistry;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/modules/{moduleType}")
    public /*ResponseEntity<ActionsResponse>*/ String getModuleActions(
            @PathVariable ModuleType moduleType,
            @RequestParam(required = false) ResourceType resourceType,
            @RequestParam(required = false) UUID resourceId,
            Authentication authentication) {

        Optional<CrmModule> module = moduleRegistry.getModule(moduleType);
        if (module.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        List<ActionDto> actions = module.get().getAvailableActionsForUser(
                userPrincipal.getId(), resourceType, resourceId);

        ActionsResponse response = ActionsResponse.builder()
                .moduleType(moduleType)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .actions(actions)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasPermission(#userId, 'USERS', 'READ')")
    public ResponseEntity<Map<ModuleType, List<ActionDto>>> getUserActions(
            @PathVariable UUID userId,
            @RequestParam(required = false) ModuleType moduleType,
            Authentication authentication) {

        Map<ModuleType, List<ActionDto>> userActions = new HashMap<>();

        List<CrmModule> modules = moduleType != null ?
                Collections.singletonList(moduleRegistry.getModule(moduleType).orElse(null)) :
                moduleRegistry.getEnabledModules();

        for (CrmModule crmModule : modules) {
            if (crmModule != null) {
                List<ActionDto> actions = crmModule.getAvailableActionsForUser(userId, null, null);
                userActions.put(crmModule.getModuleType(), actions);
            }
        }

        return ResponseEntity.ok(userActions);
    }
}

