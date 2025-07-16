package idmpartners.kz.crm.back.services;

import idmpartners.kz.crm.back.entities.Permission;
import idmpartners.kz.crm.back.entities.Role;
import idmpartners.kz.crm.back.entities.UserRole;
import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.PermissionType;
import idmpartners.kz.crm.back.enums.ResourceType;
import idmpartners.kz.crm.back.repositories.PermissionRepository;
import idmpartners.kz.crm.back.repositories.RoleRepository;
import idmpartners.kz.crm.back.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    @Cacheable(value = "userPermissions", key = "#userId + '_' + #resourceType + '_' + #actionType")
    public boolean hasPermission(UUID userId, ResourceType resourceType, ActionType actionType) {
        return hasPermission(userId, resourceType, actionType, null);
    }

    @Cacheable(value = "userPermissions", key = "#userId + '_' + #resourceType + '_' + #actionType + '_' + #targetId")
    public boolean hasPermission(UUID userId, ResourceType resourceType, ActionType actionType, Object target) {
        Set<PermissionType> userPermissions = getUserEffectivePermissions(userId, getModuleForResource(resourceType));

        PermissionType requiredPermission = findPermissionType(resourceType, actionType);

        if (requiredPermission == null) {
            return false;
        }

        boolean hasBasicPermission = userPermissions.contains(requiredPermission);

        if (!hasBasicPermission) {
            return false;
        }

        if (target != null) {
            return hasResourceScopeAccess(userId, resourceType, target);
        }

        return true;
    }

    public Set<PermissionType> getUserEffectivePermissions(UUID userId, ModuleType moduleType) {
        Set<PermissionType> effectivePermissions = new HashSet<>();

        List<UserRole> userRoles = userRoleRepository.findByUserIdAndActiveTrue(userId);

        for (UserRole userRole : userRoles) {
            Set<PermissionType> rolePermissions = getRolePermissionsRecursive(userRole.getRole());

            if (moduleType != null) {
                rolePermissions = rolePermissions.stream()
                        .filter(p -> getModuleForPermission(p) == moduleType)
                        .collect(Collectors.toSet());
            }

            effectivePermissions.addAll(rolePermissions);
        }

        return effectivePermissions;
    }

    public Set<ModuleType> getUserAccessibleModules(UUID userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndActiveTrue(userId);

        return userRoles.stream()
                .map(userRole -> userRole.getRole().getModuleType())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<PermissionType> getRolePermissionsRecursive(Role role) {
        Set<PermissionType> permissions = new HashSet<>();

        for (Permission permission : role.getPermissions()) {
            permissions.add(permission.getPermissionType());
        }

        if (role.getParentRole() != null) {
            permissions.addAll(getRolePermissionsRecursive(role.getParentRole()));
        }

        return permissions;
    }

    private boolean hasResourceScopeAccess(UUID userId, ResourceType resourceType, Object target) {
        //TODO implement Scope based access
        return true;
    }

    private PermissionType findPermissionType(ResourceType resourceType, ActionType actionType) {
        return Arrays.stream(PermissionType.values())
                .filter(p -> isPermissionMatch(p, resourceType, actionType))
                .findFirst()
                .orElse(null);
    }

    private boolean isPermissionMatch(PermissionType permission, ResourceType resourceType, ActionType actionType) {
        String permissionCode = permission.getPermissionKey();
        String expectedCode = resourceType.getCode() + ":" + actionType.getCode();
        return permissionCode.equals(expectedCode);
    }

    private ModuleType getModuleForResource(ResourceType resourceType) {
        return switch (resourceType) {
            case LEADS, DEALS, CLIENTS, SALES_REPORTS -> ModuleType.SALES;
            case EMPLOYEES, ATTENDANCE, PAYROLL, PERFORMANCE -> ModuleType.HR;
            case INVOICES, PAYMENTS, EXPENSES, BUDGETS -> ModuleType.FINANCE;
            case TICKETS, ASSETS, USERS, SYSTEMS -> ModuleType.IT;
            case CAMPAIGNS, LEADS_MARKETING, ANALYTICS, CONTENT -> ModuleType.MARKETING;
            case PROJECTS, TASKS, WORKFLOWS, DOCUMENTS -> ModuleType.OPERATIONS;
        };
    }


    private ModuleType getModuleForPermission(PermissionType permission) {
        String code = permission.name();

        if (code.startsWith("LEADS_")      || code.startsWith("DEALS_")
                || code.startsWith("CLIENTS_")      || code.startsWith("SALES_REPORTS_")) {
            return ModuleType.SALES;

        } else if (code.startsWith("EMPLOYEES_")    || code.startsWith("ATTENDANCE_")
                || code.startsWith("PAYROLL_")      || code.startsWith("PERFORMANCE_")) {
            return ModuleType.HR;

        } else if (code.startsWith("INVOICES_")     || code.startsWith("PAYMENTS_")
                || code.startsWith("EXPENSES_")     || code.startsWith("BUDGETS_")) {
            return ModuleType.FINANCE;

        } else if (code.startsWith("TICKETS_")      || code.startsWith("ASSETS_")
                || code.startsWith("USERS_")        || code.startsWith("SYSTEMS_")) {
            return ModuleType.IT;

        } else if (code.startsWith("CAMPAIGNS_")    || code.startsWith("LEADS_MARKETING_")
                || code.startsWith("ANALYTICS_")    || code.startsWith("CONTENT_")) {
            return ModuleType.MARKETING;

        } else if (code.startsWith("PROJECTS_")     || code.startsWith("TASKS_")
                || code.startsWith("WORKFLOWS_")    || code.startsWith("DOCUMENTS_")) {
            return ModuleType.OPERATIONS;

        } else if (code.startsWith("REPORT_")       || code.startsWith("DASHBOARD_")) {
            return ModuleType.ANALYTICS;

        } else {
            return ModuleType.CORE;
        }
    }

    @CacheEvict(value = "userPermissions", allEntries = true)
    public void invalidateUserPermissions(UUID userId) {
    }
}