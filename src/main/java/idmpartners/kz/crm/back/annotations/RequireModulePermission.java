package idmpartners.kz.crm.back.annotations;

import idmpartners.kz.crm.back.enums.ActionType;
import idmpartners.kz.crm.back.enums.ModuleType;
import idmpartners.kz.crm.back.enums.ResourceType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#resourceType, #actionType)")
public @interface RequireModulePermission {
    ModuleType module();
    ResourceType resourceType();
    ActionType actionType();
}
