package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ModuleInitializedEvent extends ModuleEvent {
    private final String version;
    private final int actionCount;

    public ModuleInitializedEvent(CrmModule module, String version, int actionCount) {
        super(module, "MODULE_INITIALIZED");
        this.version = version;
        this.actionCount = actionCount;
    }
}
