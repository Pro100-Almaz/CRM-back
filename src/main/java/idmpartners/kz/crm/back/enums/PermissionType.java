package idmpartners.kz.crm.back.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PermissionType {
    LEADS_CREATE(ResourceType.LEADS, ActionType.CREATE),
    LEADS_READ(ResourceType.LEADS, ActionType.READ),
    LEADS_UPDATE(ResourceType.LEADS, ActionType.UPDATE),
    LEADS_DELETE(ResourceType.LEADS, ActionType.DELETE),
    LEADS_CONVERT(ResourceType.LEADS, ActionType.CONVERT),

    DEALS_CREATE(ResourceType.DEALS, ActionType.CREATE),
    DEALS_READ(ResourceType.DEALS, ActionType.READ),
    DEALS_UPDATE(ResourceType.DEALS, ActionType.UPDATE),
    DEALS_DELETE(ResourceType.DEALS, ActionType.DELETE),
    DEALS_APPROVE(ResourceType.DEALS, ActionType.APPROVE),

    CLIENTS_CREATE(ResourceType.CLIENTS, ActionType.CREATE),
    CLIENTS_READ(ResourceType.CLIENTS, ActionType.READ),
    CLIENTS_UPDATE(ResourceType.CLIENTS, ActionType.UPDATE),
    CLIENTS_DELETE(ResourceType.CLIENTS, ActionType.DELETE),

    SALES_REPORTS_READ(ResourceType.SALES_REPORTS, ActionType.READ),
    SALES_REPORTS_EXPORT(ResourceType.SALES_REPORTS, ActionType.EXPORT),

    EMPLOYEES_CREATE(ResourceType.EMPLOYEES, ActionType.CREATE),
    EMPLOYEES_READ(ResourceType.EMPLOYEES, ActionType.READ),
    EMPLOYEES_UPDATE(ResourceType.EMPLOYEES, ActionType.UPDATE),
    EMPLOYEES_DELETE(ResourceType.EMPLOYEES, ActionType.DELETE),

    ATTENDANCE_READ(ResourceType.ATTENDANCE, ActionType.READ),
    ATTENDANCE_MANAGE(ResourceType.ATTENDANCE, ActionType.MANAGE),

    PAYROLL_READ(ResourceType.PAYROLL, ActionType.READ),
    PAYROLL_MANAGE(ResourceType.PAYROLL, ActionType.MANAGE),

    INVOICES_CREATE(ResourceType.INVOICES, ActionType.CREATE),
    INVOICES_READ(ResourceType.INVOICES, ActionType.READ),
    INVOICES_UPDATE(ResourceType.INVOICES, ActionType.UPDATE),
    INVOICES_DELETE(ResourceType.INVOICES, ActionType.DELETE),
    INVOICES_APPROVE(ResourceType.INVOICES, ActionType.APPROVE),

    PAYMENTS_READ(ResourceType.PAYMENTS, ActionType.READ),
    PAYMENTS_MANAGE(ResourceType.PAYMENTS, ActionType.MANAGE),

    TICKETS_CREATE(ResourceType.TICKETS, ActionType.CREATE),
    TICKETS_READ(ResourceType.TICKETS, ActionType.READ),
    TICKETS_UPDATE(ResourceType.TICKETS, ActionType.UPDATE),
    TICKETS_ASSIGN(ResourceType.TICKETS, ActionType.ASSIGN),

    ASSETS_CREATE(ResourceType.ASSETS, ActionType.CREATE),
    ASSETS_READ(ResourceType.ASSETS, ActionType.READ),
    ASSETS_UPDATE(ResourceType.ASSETS, ActionType.UPDATE),
    ASSETS_DELETE(ResourceType.ASSETS, ActionType.DELETE),

    USERS_CREATE(ResourceType.USERS, ActionType.CREATE),
    USERS_READ(ResourceType.USERS, ActionType.READ),
    USERS_UPDATE(ResourceType.USERS, ActionType.UPDATE),
    USERS_DELETE(ResourceType.USERS, ActionType.DELETE),

    CAMPAIGNS_CREATE(ResourceType.CAMPAIGNS, ActionType.CREATE),
    CAMPAIGNS_READ(ResourceType.CAMPAIGNS, ActionType.READ),
    CAMPAIGNS_UPDATE(ResourceType.CAMPAIGNS, ActionType.UPDATE),
    CAMPAIGNS_DELETE(ResourceType.CAMPAIGNS, ActionType.DELETE),

    ANALYTICS_READ(ResourceType.ANALYTICS, ActionType.READ),
    ANALYTICS_EXPORT(ResourceType.ANALYTICS, ActionType.EXPORT),

    PROJECTS_CREATE(ResourceType.PROJECTS, ActionType.CREATE),
    PROJECTS_READ(ResourceType.PROJECTS, ActionType.READ),
    PROJECTS_UPDATE(ResourceType.PROJECTS, ActionType.UPDATE),
    PROJECTS_DELETE(ResourceType.PROJECTS, ActionType.DELETE),

    TASKS_CREATE(ResourceType.TASKS, ActionType.CREATE),
    TASKS_READ(ResourceType.TASKS, ActionType.READ),
    TASKS_UPDATE(ResourceType.TASKS, ActionType.UPDATE),
    TASKS_ASSIGN(ResourceType.TASKS, ActionType.ASSIGN);

    private final ResourceType resource;
    private final ActionType action;

    PermissionType(ResourceType resource, ActionType action) {
        this.resource = resource;
        this.action = action;
    }

    public ModuleType getModule() { return resource.getModule(); }

    public String getPermissionKey() {
        return resource.getCode() + ":" + action.getCode();
    }

    public static PermissionType fromKey(String key) {
        String[] parts = key.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid permission key: " + key);
        }

        ResourceType resource = ResourceType.fromCode(parts[0]);
        ActionType action = ActionType.fromCode(parts[1]);

        return Arrays.stream(values())
                .filter(p -> p.resource == resource && p.action == action)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown permission: " + key));
    }

    public static List<PermissionType> getByModule(ModuleType module) {
        return Arrays.stream(values())
                .filter(p -> p.getModule() == module)
                .toList();
    }

}