package tech.kvothe.leaderboard.dto;

import jakarta.validation.constraints.NotNull;

public record RecoveryUserDto(@NotNull Long id,
                              @NotNull String userName) {
}
