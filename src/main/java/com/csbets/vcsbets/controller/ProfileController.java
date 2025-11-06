package com.csbets.vcsbets.controller;

import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PutMapping
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("User saved successfully.");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
