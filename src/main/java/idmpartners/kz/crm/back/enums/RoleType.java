package idmpartners.kz.crm.back.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum RoleType {
    OWNER("OWNER", "Owner", null),
    DIRECTOR("DIRECTOR", "Director", OWNER),

    // Department heads
    SALES_DIRECTOR("SALES_DIRECTOR", "Sales Director", DIRECTOR),
    HR_DIRECTOR("HR_DIRECTOR", "HR Director", DIRECTOR),
    FINANCE_DIRECTOR("FINANCE_DIRECTOR", "Finance Director", DIRECTOR),
    IT_DIRECTOR("IT_DIRECTOR", "IT Director", DIRECTOR),
    MARKETING_DIRECTOR("MARKETING_DIRECTOR", "Marketing Director", DIRECTOR),
    OPERATIONS_DIRECTOR("OPERATIONS_DIRECTOR", "Operations Director", DIRECTOR),

    // Supervisors
    SALES_SUPERVISOR("SALES_SUPERVISOR", "Sales Supervisor", SALES_DIRECTOR),
    HR_SUPERVISOR("HR_SUPERVISOR", "HR Supervisor", HR_DIRECTOR),
    FINANCE_SUPERVISOR("FINANCE_SUPERVISOR", "Finance Supervisor", FINANCE_DIRECTOR),
    IT_SUPERVISOR("IT_SUPERVISOR", "IT Supervisor", IT_DIRECTOR),
    MARKETING_SUPERVISOR("MARKETING_SUPERVISOR", "Marketing Supervisor", MARKETING_DIRECTOR),
    OPERATIONS_SUPERVISOR("OPERATIONS_SUPERVISOR", "Operations Supervisor", OPERATIONS_DIRECTOR),

    // Employees
    SALES_MANAGER("SALES_MANAGER", "Sales Manager", SALES_SUPERVISOR),
    HR_SPECIALIST("HR_SPECIALIST", "HR Specialist", HR_SUPERVISOR),
    ACCOUNTANT("ACCOUNTANT", "Accountant", FINANCE_SUPERVISOR),
    IT_SPECIALIST("IT_SPECIALIST", "IT Specialist", IT_SUPERVISOR),
    MARKETING_SPECIALIST("MARKETING_SPECIALIST", "Marketing Specialist", MARKETING_SUPERVISOR),
    OPERATIONS_SPECIALIST("OPERATIONS_SPECIALIST", "Operations Specialist", OPERATIONS_SUPERVISOR);

    private final String code;
    private final String displayName;
    private final RoleType parent;

    RoleType(String code, String displayName, RoleType parent) {
        this.code = code;
        this.displayName = displayName;
        this.parent = parent;
    }

    public ModuleType getModule() {
        String roleCode = this.code.toLowerCase();
        if (roleCode.contains("sales")) return ModuleType.SALES;
        if (roleCode.contains("hr")) return ModuleType.HR;
        if (roleCode.contains("finance")) return ModuleType.FINANCE;
        if (roleCode.contains("it")) return ModuleType.IT;
        if (roleCode.contains("marketing")) return ModuleType.MARKETING;
        if (roleCode.contains("operations")) return ModuleType.OPERATIONS;
        return null;
    }

    public static RoleType fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + code));
    }

    public List<RoleType> getHierarchy() {
        List<RoleType> hierarchy = new ArrayList<>();
        RoleType current = this;
        while (current != null) {
            hierarchy.add(current);
            current = current.parent;
        }
        return hierarchy;
    }
}
