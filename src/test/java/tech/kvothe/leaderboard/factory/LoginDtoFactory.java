package tech.kvothe.leaderboard.factory;

import tech.kvothe.leaderboard.dto.LoginDto;

public class LoginDtoFactory {

    public static LoginDto buildOneDto() {
        return new LoginDto(
                "kvothe",
                "kvothe123"
        );
    }
}
