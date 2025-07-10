package idmpartners.kz.crm.back.controllers;

import idmpartners.kz.crm.back.dto.UserLoginDto;
import idmpartners.kz.crm.back.dto.UserTokenDto;
import idmpartners.kz.crm.back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @PostMapping(value = "/login")
    public UserTokenDto signIn(@RequestBody UserLoginDto userLoginDto) {
        return userService.authenticate(userLoginDto);
    }
}
