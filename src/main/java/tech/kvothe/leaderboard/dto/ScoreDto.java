package tech.kvothe.leaderboard.dto;

import jakarta.validation.constraints.NotNull;

public record ScoreDto(@NotNull Double score) {
}
