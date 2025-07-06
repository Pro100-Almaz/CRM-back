package idmpartners.kz.crm.back.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ModuleType {

        SALES("sales", "Sales Management"),
        HR("hr", "Human Resources"),
        FINANCE("finance", "Finance Management"),
        IT("it", "IT Management"),
        MARKETING("marketing", "Marketing Management"),
        OPERATIONS("operations", "Operations Management"),
        ANALYTICS("analytics", "Analytics & BI"),
        CORE("core", "Core System");

        private final String code;
        private final String displayName;

        ModuleType(String code, String displayName) {
                this.code = code;
                this.displayName = displayName;
        }

        public static ModuleType fromCode(String code) {
                return Arrays.stream(values())
                        .filter(m -> m.code.equals(code))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown module: " + code));
        }
}
