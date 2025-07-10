package idmpartners.kz.crm.back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenDto {
    private String accessToken;
    private String refreshToken;
}
