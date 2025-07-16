package idmpartners.kz.crm.back.converter;

import idmpartners.kz.crm.back.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class UserUtils {
    public static Jwt getCurrentUserJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        log.warn("Curren user is not found");
        return null;
    }
    public static String getCurrentUserName() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt != null) {
            return jwt.getClaimAsString("preferred_username");

        }
        log.warn("Current user is not found");
        return null;
    }
    public static UserDto getCurrentUser() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt != null) {
            return UserDto.builder()
                    .email(jwt.getClaimAsString(""))
                    .username(jwt.getClaimAsString("preferred_username"))
                    .firstName(jwt.getClaimAsString("given_name"))
                    .lastName(jwt.getClaimAsString("family_name"))
                    .build();
        }
    }
}
