package idmpartners.kz.crm.back.controllers;

import idmpartners.kz.crm.back.dto.UserCreateDto;
import idmpartners.kz.crm.back.dto.UserDto;
import idmpartners.kz.crm.back.services.AdminService;
import idmpartners.kz.crm.back.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController{
    private final UserService userService;
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = adminService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(adminService.getUser(id));
        } catch (Exception e) {
            log.error("User not found: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/user")
    public ResponseEntity<Void> updateUser(@RequestBody UserCreateDto user) {
        try {
            adminService.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating user: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
