package idmpartners.kz.crm.back.annotations;

import idmpartners.kz.crm.back.enums.ActionType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#target, #actionType)")
public @interface RequireEntityPermission {
    ActionType actionType();
    String target() default "target";
}