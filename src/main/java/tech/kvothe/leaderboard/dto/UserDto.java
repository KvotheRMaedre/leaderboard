package tech.kvothe.leaderboard.dto;

import jakarta.validation.constraints.NotNull;

public record UserDto(@NotNull String userName,
                      @NotNull String name,
                      @NotNull String password) {
}
