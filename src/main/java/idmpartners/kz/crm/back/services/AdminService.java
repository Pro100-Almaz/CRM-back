package idmpartners.kz.crm.back.services;

import idmpartners.kz.crm.back.dto.UserCreateDto;
import idmpartners.kz.crm.back.dto.UserDto;
import idmpartners.kz.crm.back.entities.Role;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;


    public List<UserDto> getUsers() {
        List<UserRepresentation> userRepresentations =
                keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    public UserDto getUser(String id) {
        UserRepresentation userRep = keycloak.realm(realm).users().get(id).toRepresentation();
        return mapUser(userRep);
    }

    public void deleteUser(String id) {


        keycloak.realm(realm).users().delete(id);
    }

    public void updateUser(UserCreateDto user) {
        UserRepresentation userRep = mapUserRep(user);
        keycloak.realm(realm).users().get(user.getId()).update(userRep);
    }

    private List<UserDto> mapUsers(List<UserRepresentation> userRepresentations) {
        List<UserDto> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> users.add(mapUser(userRep)));
        }
        return users;
    }

    private UserDto mapUser(UserRepresentation userRep) {
        UserDto user = new UserDto();
        user.setId(userRep.getId());
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUsername(userRep.getUsername());
        return user;
    }

    private UserRepresentation mapUserRep(UserCreateDto user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(user.getId());
        userRep.setUsername(user.getUsername());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());

        List<CredentialRepresentation> creds = new ArrayList<>();
        creds.add(cred);
        userRep.setCredentials(creds);

        return userRep;
    }
}
