package tech.kvothe.leaderboard.service;

import org.springframework.stereotype.Service;
import tech.kvothe.leaderboard.dto.ScoreDto;

@Service
public class ScoreService {

    private final RedisService redisService;

    public ScoreService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Long submitScore(ScoreDto submitScoreDTO, String game, String userName) {
        return redisService.addScore(game, submitScoreDTO.score(), userName);
    }
}
