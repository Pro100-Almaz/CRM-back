package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ModuleEnabledEvent extends ModuleEvent {
    public ModuleEnabledEvent(CrmModule module) {
        super(module, "MODULE_ENABLED");
    }
}