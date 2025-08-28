package tech.kvothe.leaderboard.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.kvothe.leaderboard.dto.ScoreDto;
import tech.kvothe.leaderboard.service.RedisService;
import tech.kvothe.leaderboard.service.ScoreService;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final ScoreService scoreService;

    public LeaderboardController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("{game}/score")
    public ResponseEntity<String> submitScore(@PathVariable("game") String game,
                                            @RequestBody @Valid ScoreDto request) {
        String message;
        var response = scoreService.submitScore(request, game, SecurityContextHolder.getContext().getAuthentication().getName());

        if (response == 0L)
            message = "We will only consider the last score you submitted for each leaderboard!";
        else
            message = "Score submitted!";

        return ResponseEntity.ok(message);
    }
}
