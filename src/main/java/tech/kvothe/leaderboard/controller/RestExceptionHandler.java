package tech.kvothe.leaderboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.kvothe.leaderboard.exception.LeaderboardException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(LeaderboardException.class)
    public ProblemDetail handleLeaderboardException(LeaderboardException leaderboardException){
        return leaderboardException.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        var fieldErrors = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(fieldError -> new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Your request parameters didn't validate.");
        problemDetail.setProperty("invalid-params", fieldErrors);

        return problemDetail;
    }

    private record InvalidParam(String name, String reason){}

}
