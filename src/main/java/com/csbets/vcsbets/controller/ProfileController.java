package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserDtoByUsername(username));
    }

    @GetMapping("/me")
    public Object getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return userService.convertToUserDTO(user);
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("User saved successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set-pass/{username}")
    public ResponseEntity<String> setPassword(@PathVariable String username, @RequestParam String password) {
        userService.setPassword(username, password);
        return ResponseEntity.ok("Password set successfully.");
    }
}
