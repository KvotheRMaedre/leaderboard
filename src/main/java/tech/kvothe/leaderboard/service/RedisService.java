package tech.kvothe.leaderboard.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import tech.kvothe.leaderboard.dto.ScoreDto;

@Service
public class RedisService {

    private final UnifiedJedis jedis;

    public RedisService() {
        this.jedis = new UnifiedJedis("redis://" + System.getenv("REDIS_URL"));
    }

    public Long addScore(String game, double score, String userName) {
        var leaderboard = "leaderboard:" + game;
        return jedis.zadd(leaderboard, score, userName);
    }
}
