package tech.kvothe.leaderboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundInLeaderboardException extends LeaderboardException {

    private final String game;
    private final String name;

    public UserNotFoundInLeaderboardException(String game, String name) {
        this.game = game;
        this.name = name;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetails.setTitle("User is not part of the leaderboard");
        problemDetails.setDetail("The user " + name + " has not taken part of the " + game + " leaderboard.");

        return problemDetails;
    }
}
