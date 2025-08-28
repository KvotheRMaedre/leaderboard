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

    public Long getUserRaking(String game, String userName) {
        var leaderboard = "leaderboard:" + game;
        return jedis.zrevrank(leaderboard, userName) + 1;
    }

    public boolean userExistsInLeaderboard(String game, String userName) {
        var leaderboard = "leaderboard:" + game;
        var result =  jedis.zscore(leaderboard, userName);
        return result != null;
    }
}
