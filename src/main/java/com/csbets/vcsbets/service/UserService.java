package com.csbets.vcsbets.service;

import com.csbets.vcsbets.dto.user.UserDto;
import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .toList();
    }

    public UserDto getUserDtoByUsername(String username) {
        return convertToUserDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден")));
    }

    private UserDto convertToUserDTO(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getCreditBalance(),
                user.getWinningsBalance(),
                user.getPlacedBetsCount(),
                user.getBetsWinRate(),
                user.getRole()
        );
    }
}
