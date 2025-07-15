package idmpartners.kz.crm.back.controllers;


import idmpartners.kz.crm.back.converter.UserUtils;
import idmpartners.kz.crm.back.dto.*;
import idmpartners.kz.crm.back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping(value ="/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.OK);
    }
    @PostMapping(value = "/login")
    public UserTokenDto signIn(@RequestBody UserLoginDto userLoginDto) {
        return userService.authenticate(userLoginDto);
    }
    @GetMapping(value = "/current-username")
    public String getCurrentUsername() {
        return UserUtils.getCurrentUserName();
    }

    @GetMapping(value = "/current-user")
    public UserDto getCurrentUser() {
        return UserUtils.getCurrentUser();
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {
        String userName = UserUtils.getCurrentUserName();
        if(userName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not find user");
        }
        try{
            userService.changePassword(userName, userChangePasswordDto.getNewPassword());
            return ResponseEntity.ok("Password changed");
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }
}
