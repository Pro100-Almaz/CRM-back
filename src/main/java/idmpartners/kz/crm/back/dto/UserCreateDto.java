package idmpartners.kz.crm.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
}
