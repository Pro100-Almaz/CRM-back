package idmpartners.kz.crm.back.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordDto {
    private String newPassword;
}
