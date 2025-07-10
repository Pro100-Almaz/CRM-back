package idmpartners.kz.crm.back.client;

import idmpartners.kz.crm.back.dto.UserCreateDto;
import idmpartners.kz.crm.back.dto.UserLoginDto;
import idmpartners.kz.crm.back.dto.UserTokenDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyCloakClient {
    private final Keycloak keycloak;
    private final RestTemplate restTemplate;
    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String clientId;

    @Value("${keycloak.secret}")
    private String clientSecret;

    public UserRepresentation createUser(UserCreateDto userCreateDto) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(userCreateDto.getUsername());
        newUser.setFirstName(userCreateDto.getFirstName());
        newUser.setLastName(userCreateDto.getLastName());
        newUser.setEnabled(true);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(userCreateDto.getPassword());
        credentials.setTemporary(false);

        newUser.setCredentials(List.of(credentials));

        Response response = keycloak
                .realm(realm)
                .users()
                .create(newUser);
        if (response.getStatus() != HttpStatus.CREATED.value()){
            log.error("Error creating the user: {}", response.getStatus());
            throw new RuntimeException("Error creating the user: " + response.getStatus());
        }
        List<UserRepresentation> userList = keycloak.realm(realm).users().search(userCreateDto.getUsername());
        return userList.get(0);


    }
    public UserTokenDto signIn(UserLoginDto userLoginDto){
        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", userLoginDto.getUsername());
        formData.add("password", userLoginDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint,
                new HttpEntity<>(formData, headers), Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error creating the user: {}", userLoginDto.getUsername());
            throw new RuntimeException("Error creating the user: " + response.getStatusCode());
        }

        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setAccessToken((String) responseBody.get("access_token"));
        userTokenDto.setRefreshToken((String) responseBody.get("refresh_token"));
        return userTokenDto;

    }
    public void changePassword(String username, String password) {
        List<UserRepresentation> userList = keycloak.realm(realm).users().search(username);
        if (userList.isEmpty()) {
            log.error("User with this username {} not found", username);
            throw new RuntimeException("User with this username " +username + " not found");
        }
        UserRepresentation userRepresentation = userList.get(0);
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        credentials.setTemporary(false);
        keycloak.realm(realm).users().get(userRepresentation.getId()).resetPassword(credentials);
        log.info("Changed password for user {}", username);

    }
}
