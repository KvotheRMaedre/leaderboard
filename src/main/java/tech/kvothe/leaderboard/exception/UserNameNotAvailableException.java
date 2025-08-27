package tech.kvothe.leaderboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNameNotAvailableException extends LeaderboardException {

    private final String userName;

    public UserNameNotAvailableException(String userName) {
        this.userName = userName;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetails = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetails.setTitle("Username not available");
        problemDetails.setDetail("The username " + userName + " is already taken");

        return problemDetails;
    }
}
