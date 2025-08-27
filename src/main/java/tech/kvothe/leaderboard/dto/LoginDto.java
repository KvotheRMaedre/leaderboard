package tech.kvothe.leaderboard.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDto(@NotNull String userName,
                       @NotNull String password) {
}
