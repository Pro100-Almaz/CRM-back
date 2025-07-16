package idmpartners.kz.crm.back.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum RoleType {
    OWNER("OWNER", "Owner", null, null),
    DIRECTOR("DIRECTOR", "Director", OWNER, null),

    SALES_DIRECTOR("SALES_DIRECTOR", "Sales Director", DIRECTOR, ModuleType.SALES),
    HR_DIRECTOR("HR_DIRECTOR", "HR Director", DIRECTOR, ModuleType.HR),
    FINANCE_DIRECTOR("FINANCE_DIRECTOR", "Finance Director", DIRECTOR, ModuleType.FINANCE),
    IT_DIRECTOR("IT_DIRECTOR", "IT Director", DIRECTOR, ModuleType.IT),
    MARKETING_DIRECTOR("MARKETING_DIRECTOR", "Marketing Director", DIRECTOR, ModuleType.MARKETING),
    OPERATIONS_DIRECTOR("OPERATIONS_DIRECTOR", "Operations Director", DIRECTOR, ModuleType.OPERATIONS),

    SALES_SUPERVISOR("SALES_SUPERVISOR", "Sales Supervisor", SALES_DIRECTOR, ModuleType.SALES),
    HR_SUPERVISOR("HR_SUPERVISOR", "HR Supervisor", HR_DIRECTOR, ModuleType.HR),
    FINANCE_SUPERVISOR("FINANCE_SUPERVISOR", "Finance Supervisor", FINANCE_DIRECTOR, ModuleType.FINANCE),
    IT_SUPERVISOR("IT_SUPERVISOR", "IT Supervisor", IT_DIRECTOR, ModuleType.IT),
    MARKETING_SUPERVISOR("MARKETING_SUPERVISOR", "Marketing Supervisor", MARKETING_DIRECTOR, ModuleType.MARKETING),
    OPERATIONS_SUPERVISOR("OPERATIONS_SUPERVISOR", "Operations Supervisor", OPERATIONS_DIRECTOR, ModuleType.OPERATIONS),

    SALES_MANAGER("SALES_MANAGER", "Sales Manager", SALES_SUPERVISOR, ModuleType.SALES),
    HR_SPECIALIST("HR_SPECIALIST", "HR Specialist", HR_SUPERVISOR, ModuleType.HR),
    ACCOUNTANT("ACCOUNTANT", "Accountant", FINANCE_SUPERVISOR, ModuleType.FINANCE),
    IT_SPECIALIST("IT_SPECIALIST", "IT Specialist", IT_SUPERVISOR, ModuleType.IT),
    MARKETING_SPECIALIST("MARKETING_SPECIALIST", "Marketing Specialist", MARKETING_SUPERVISOR, ModuleType.MARKETING),
    OPERATIONS_SPECIALIST("OPERATIONS_SPECIALIST", "Operations Specialist", OPERATIONS_SUPERVISOR, ModuleType.OPERATIONS);

    private final String code;
    private final String displayName;
    private final RoleType parent;
    private final ModuleType module;

    RoleType(String code, String displayName, RoleType parent, ModuleType module) {
        this.code = code;
        this.displayName = displayName;
        this.parent = parent;
        this.module = module;
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

    /**
     * Get all roles that belong to a specific module
     */
    public static List<RoleType> getByModule(ModuleType moduleType) {
        return Arrays.stream(values())
                .filter(role -> role.module == moduleType)
                .toList();
    }

    /**
     * Check if this role has access to a specific module
     * (either directly assigned or through hierarchy)
     */
    public boolean hasModuleAccess(ModuleType moduleType) {
        if (this.module == moduleType) {
            return true;
        }

        // Check if any parent role has access to this module
        RoleType current = this.parent;
        while (current != null) {
            if (current.module == moduleType) {
                return true;
            }
            current = current.parent;
        }

        return false;
    }

    /**
     * Get all modules this role has access to (including through hierarchy)
     */
    public List<ModuleType> getAccessibleModules() {
        List<ModuleType> modules = new ArrayList<>();
        RoleType current = this;

        while (current != null) {
            if (current.module != null && !modules.contains(current.module)) {
                modules.add(current.module);
            }
            current = current.parent;
        }

        return modules;
    }

    /**
     * Check if this role is a department head (Director level)
     */
    public boolean isDepartmentHead() {
        return this.code.endsWith("_DIRECTOR");
    }

    /**
     * Check if this role is a supervisor
     */
    public boolean isSupervisor() {
        return this.code.endsWith("_SUPERVISOR");
    }

    /**
     * Check if this role is an employee (leaf level)
     */
    public boolean isEmployee() {
        return !isDepartmentHead() && !isSupervisor() &&
                !this.code.equals("OWNER") && !this.code.equals("DIRECTOR");
    }
}