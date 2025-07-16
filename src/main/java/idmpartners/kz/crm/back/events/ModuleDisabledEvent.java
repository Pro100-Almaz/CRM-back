package idmpartners.kz.crm.back.events;

import idmpartners.kz.crm.back.interfaces.CrmModule;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ModuleDisabledEvent extends ModuleEvent {
    public ModuleDisabledEvent(CrmModule module) {
        super(module, "MODULE_DISABLED");
    }
}
