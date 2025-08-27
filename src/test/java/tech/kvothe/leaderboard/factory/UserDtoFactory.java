package tech.kvothe.leaderboard.factory;

import tech.kvothe.leaderboard.dto.UserDto;

public class UserDtoFactory {

    public static UserDto buildOneUserDto() {
       return new UserDto(
                "kvothe",
                "Kvothe Red Maedre",
                "kvothe123"
        );

    }
}
