package tech.kvothe.leaderboard.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.kvothe.leaderboard.dto.ApiTextResponse;
import tech.kvothe.leaderboard.dto.ScoreDto;
import tech.kvothe.leaderboard.service.ScoreService;
import tech.kvothe.leaderboard.service.UserService;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final ScoreService scoreService;
    private final UserService userService;

    public LeaderboardController(ScoreService scoreService, UserService userService) {
        this.scoreService = scoreService;
        this.userService = userService;
    }

    @PostMapping("{game}/score")
    public ResponseEntity<ApiTextResponse> submitScore(@PathVariable("game") String game,
                                            @RequestBody @Valid ScoreDto request) {
        String message;
        var response = scoreService.submitScore(request, game, SecurityContextHolder.getContext().getAuthentication().getName());

        if (response == 0L)
            message = "We will only consider the last score you submitted for each leaderboard!";
        else
            message = "Score submitted!";

        return ResponseEntity.ok(new ApiTextResponse(message));
    }

    @GetMapping(path = "{game}/rank", produces = "application/json")
    public ResponseEntity<String> getUserRanking(@PathVariable("game") String game) {
        var response = userService.getUserRaking(game, SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.ok("{ \"rank\": \""+ response +"\" }");
    }
}
