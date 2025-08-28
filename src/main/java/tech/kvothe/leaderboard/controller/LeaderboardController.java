package tech.kvothe.leaderboard.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.kvothe.leaderboard.dto.ScoreDto;
import tech.kvothe.leaderboard.service.RedisService;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final RedisService redisService;

    public LeaderboardController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("{game}/score")
    public ResponseEntity<Void> submitScore(@PathVariable("game") String game,
                                            @RequestBody @Valid ScoreDto request) {
        redisService.submitScore(request, game, SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok().build();
    }
}
