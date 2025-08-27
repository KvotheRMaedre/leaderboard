package tech.kvothe.leaderboard.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tech.kvothe.leaderboard.dto.RecoveryJwtTokenDto;
import tech.kvothe.leaderboard.dto.UserDto;
import tech.kvothe.leaderboard.entity.User;
import tech.kvothe.leaderboard.exception.UserNameNotAvailableException;
import tech.kvothe.leaderboard.repository.UserRepository;
import tech.kvothe.leaderboard.security.authentication.JwtTokenService;
import tech.kvothe.leaderboard.security.config.SecurityConfiguration;
import tech.kvothe.leaderboard.security.userdetails.UserDetailsImpl;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserRepository userRepository, SecurityConfiguration securityConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.securityConfiguration = securityConfiguration;
    }

    public RecoveryJwtTokenDto authenticateUser(UserDto userDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.userName(), userDto.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(UserDto userDTO) {

        validateUserName(userDTO.userName());

        var user = new User(
                userDTO.userName(),
                userDTO.name(),
                securityConfiguration.passwordEncoder().encode(userDTO.password())
        );

        userRepository.save(user);
    }

    void validateUserName(String userName) {
        var user = userRepository.findByUserName(userName);
        if (user.isPresent())
            throw new UserNameNotAvailableException(userName);
    }
}
