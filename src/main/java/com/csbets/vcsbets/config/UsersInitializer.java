package com.csbets.vcsbets.config;

import com.csbets.vcsbets.dto.user.UserSeedDto;
import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UsersInitializer {

    private final UserRepository userRepository;
    private static final List<UserSeedDto> USERS = List.of(
            new UserSeedDto(2189726185L, "frixieq", "https://steamcommunity.com/id/frixieqqq/"),
            new UserSeedDto(2189856549L, "whynot", "https://steamcommunity.com/id/whynotishe/"),
            new UserSeedDto(2189957123L, "Deserma", "https://steamcommunity.com/id/deserma/"),
            new UserSeedDto(2190023456L, "Scuderia", "https://steamcommunity.com/profiles/76561199054745302/"),
            new UserSeedDto(2190156789L, "expiasian", "https://steamcommunity.com/profiles/76561199641451140/"),
            new UserSeedDto(2190222333L, "rel1segod", "https://steamcommunity.com/id/rel1seg0d"),
            new UserSeedDto(2190333444L, "YouMore", "https://steamcommunity.com/profiles/76561199212124731/"),
            new UserSeedDto(2190444555L, "WhatIsLove", "https://steamcommunity.com/profiles/76561198295068808/"),
            new UserSeedDto(2190555666L, "Vinni Boombatz", "https://steamcommunity.com/id/KazanMangal/"),
            new UserSeedDto(2190666777L, "nikno8", "https://steamcommunity.com/id/nikitalaguT/"),
            new UserSeedDto(2190777888L, "3Be3Da yIIaJla上帝", "https://steamcommunity.com/profiles/76561198831754010/"),
            new UserSeedDto(2190888999L, "Sa1der", "https://steamcommunity.com/profiles/76561199529515417/"),
            new UserSeedDto(2190999111L, "ryousuke", "https://steamcommunity.com/id/34125679765/"),
            new UserSeedDto(2191111222L, "yayebok228", "https://steamcommunity.com/id/trader86/"),
            new UserSeedDto(2191222333L, "Sh1ny", "https://steamcommunity.com/profiles/76561198390797625/"),
            new UserSeedDto(2191333444L, "Lexx_Tr1ck", "https://steamcommunity.com/id/RogBobins/")
    );

    @PostConstruct
    public void seedUsers() {
        if (userRepository.count() <= 1) {
            USERS.forEach(seed -> {
                User user = new User();
                user.setId(seed.id());
                user.setUsername(seed.username());
                user.setSteamLink(seed.steamLink());
                userRepository.save(user);
            });
        }
    }
}
