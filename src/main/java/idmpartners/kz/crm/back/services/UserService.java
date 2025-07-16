package idmpartners.kz.crm.back.services;

import idmpartners.kz.crm.back.client.KeyCloakClient;
import idmpartners.kz.crm.back.dto.UserCreateDto;
import idmpartners.kz.crm.back.dto.UserDto;
import idmpartners.kz.crm.back.dto.UserLoginDto;
import idmpartners.kz.crm.back.dto.UserTokenDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    final private KeyCloakClient keyCloakClient;

    public UserDto createUser(UserCreateDto userCreateDto){
        UserRepresentation userRepresentation = keyCloakClient.createUser(userCreateDto);
        UserDto userDto = new UserDto();
        userDto.setUsername(userRepresentation.getUsername());
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setLastName(userRepresentation.getLastName());

        return userDto;
    }
    public UserTokenDto authenticate(UserLoginDto userLoginDto){
        return keyCloakClient.signIn(userLoginDto);
    }
    public void changePassword(String userName, String password){
        keyCloakClient.changePassword(userName, password);
    }
}
