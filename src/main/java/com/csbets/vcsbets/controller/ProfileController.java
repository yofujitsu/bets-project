package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.dto.user.UserDto;
import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userService.getUserDtoByUsername(username);
    }

    @GetMapping("/me")
    public UserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return userService.convertToUserDTO(user);
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public void saveUser(@RequestBody User user) {
        userService.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set-pass/{username}")
    public void setPassword(@PathVariable String username, @RequestParam String password) {
        userService.setPassword(username, password);
    }
}
