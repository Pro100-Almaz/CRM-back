package idmpartners.kz.crm.back.controllers;

import idmpartners.kz.crm.back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController{
    private final UserService userService;

    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/user-list")
    public List<String> userList(){
        return Arrays.asList("user1", "user2", "user3");
    }



}
