package com.csbets.vcsbets.dto.user;

import com.csbets.vcsbets.entity.user.UserRole;

public record UserDto(
        Long id,
        String username,
        String steamLink,
        short creditBalance,
        short winningsBalance,
        int placedBetsCount,
        double betsWinRate,
        UserRole role
) {
}
