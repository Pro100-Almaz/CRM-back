package idmpartners.kz.crm.back.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ResourceType {
    LEADS("leads", ModuleType.SALES),
    DEALS("deals", ModuleType.SALES),
    CLIENTS("clients", ModuleType.SALES),
    SALES_REPORTS("sales_reports", ModuleType.SALES),

    // HR resources
    EMPLOYEES("employees", ModuleType.HR),
    ATTENDANCE("attendance", ModuleType.HR),
    PAYROLL("payroll", ModuleType.HR),
    PERFORMANCE("performance", ModuleType.HR),

    // Finance resources
    INVOICES("invoices", ModuleType.FINANCE),
    PAYMENTS("payments", ModuleType.FINANCE),
    EXPENSES("expenses", ModuleType.FINANCE),
    BUDGETS("budgets", ModuleType.FINANCE),

    // IT resources
    TICKETS("tickets", ModuleType.IT),
    ASSETS("assets", ModuleType.IT),
    USERS("users", ModuleType.IT),
    SYSTEMS("systems", ModuleType.IT),

    // Marketing resources
    CAMPAIGNS("campaigns", ModuleType.MARKETING),
    LEADS_MARKETING("leads_marketing", ModuleType.MARKETING),
    ANALYTICS("analytics", ModuleType.MARKETING),
    CONTENT("content", ModuleType.MARKETING),

    // Operations resources
    PROJECTS("projects", ModuleType.OPERATIONS),
    TASKS("tasks", ModuleType.OPERATIONS),
    WORKFLOWS("workflows", ModuleType.OPERATIONS),
    DOCUMENTS("documents", ModuleType.OPERATIONS);

    private final String code;
    private final ModuleType module;

    ResourceType(String code, ModuleType module) {
        this.code = code;
        this.module = module;
    }

    public static ResourceType fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown resource: " + code));
    }

    public static List<ResourceType> getByModule(ModuleType module) {
        return Arrays.stream(values())
                .filter(r -> r.module == module)
                .toList();
    }
}
