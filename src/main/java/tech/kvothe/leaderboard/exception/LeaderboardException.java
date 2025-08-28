package tech.kvothe.leaderboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LeaderboardException extends RuntimeException {

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Leaderboard internal server error");

        return problemDetail;
    }
}
