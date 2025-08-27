package tech.kvothe.leaderboard.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kvothe.leaderboard.dto.LoginDto;
import tech.kvothe.leaderboard.dto.RecoveryJwtTokenDto;
import tech.kvothe.leaderboard.dto.UserDto;
import tech.kvothe.leaderboard.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto request) {
        userService.createUser(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(){
        return null;
    }
}
