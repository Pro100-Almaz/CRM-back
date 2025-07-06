package idmpartners.kz.crm.back.enums;

import java.util.Arrays;

public enum ActionType {
    CREATE("create", "Create"),
    READ("read", "View"),
    UPDATE("update", "Edit"),
    DELETE("delete", "Delete"),
    APPROVE("approve", "Approve"),
    REJECT("reject", "Reject"),
    EXPORT("export", "Export"),
    IMPORT("import", "Import"),
    ASSIGN("assign", "Assign"),
    CONVERT("convert", "Convert"),
    ARCHIVE("archive", "Archive"),
    RESTORE("restore", "Restore"),
    MANAGE("manage", "Manage"),
    REPORT("report", "Generate Reports");

    private final String code;
    private final String displayName;

    ActionType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static ActionType fromCode(String code) {
        return Arrays.stream(values())
                .filter(a -> a.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown action: " + code));
    }
}