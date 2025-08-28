package tech.kvothe.leaderboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.kvothe.leaderboard.dto.UserDto;
import tech.kvothe.leaderboard.entity.User;
import tech.kvothe.leaderboard.exception.UserNameNotAvailableException;
import tech.kvothe.leaderboard.exception.UserNotFoundInLeaderboardException;
import tech.kvothe.leaderboard.factory.UserDtoFactory;
import tech.kvothe.leaderboard.factory.UserFactory;
import tech.kvothe.leaderboard.repository.UserRepository;
import tech.kvothe.leaderboard.security.authentication.JwtTokenService;
import tech.kvothe.leaderboard.security.config.SecurityConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserDto> userDtoArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should save user with success")
        public void shouldSaveUserWithSuccess() {
            var userDto = UserDtoFactory.buildOneUserDto();
            var user = UserFactory.buildOneUser();

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            doReturn(new BCryptPasswordEncoder())
                    .when(securityConfiguration)
                    .passwordEncoder();

            userService.createUser(userDto);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(userDto.userName(), userCaptured.getUserName());
            assertEquals(userDto.name(), userCaptured.getName());
            verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Should encode user password")
        public void shouldEncodeUserPassword() {
            var userDto = UserDtoFactory.buildOneUserDto();
            var user = UserFactory.buildOneUser();

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            doReturn(new BCryptPasswordEncoder())
                    .when(securityConfiguration)
                    .passwordEncoder();

            userService.createUser(userDto);

            var userCaptured = userArgumentCaptor.getValue();
            assertNotEquals(userDto.password(), userCaptured.getPassword());
            verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        public void shouldThrowExceptionWhenUserNameAlreadyExists() {
            var userDto = UserDtoFactory.buildOneUserDto();
            var user = UserFactory.buildOneUser();

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findByUserName(stringArgumentCaptor.capture());

            assertThrows(UserNameNotAvailableException.class, () -> userService.createUser(userDto));
        }
    }

    @Nested
    class getUserRaking {
        @Test
        @DisplayName("Should validate if user exists in leaderboard")
        public void shouldValidateIfUserExistsInLeaderboard() {
            var game = "subnautica";
            var name = "ryley";

            doReturn(true)
                    .when(redisService)
                    .userExistsInLeaderboard(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            doReturn(1L)
                    .when(redisService)
                    .getUserRaking(any(), any());

            userService.getUserRaking(game, name);

            var stringsCaptured = stringArgumentCaptor.getAllValues();

            assertEquals(game, stringsCaptured.getFirst());
            assertEquals(name, stringsCaptured.getLast());
            verify(redisService, times(1))
                    .userExistsInLeaderboard(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Should throw exception in case use doesn't exists in the leaderboard")
        public void shouldThrowExceptionInCaseUserDoesNotExistsInLeaderboard() {
            var game = "subnautica";
            var name = "ryley";

            doReturn(false)
                    .when(redisService)
                    .userExistsInLeaderboard(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            assertThrows(UserNotFoundInLeaderboardException.class, () ->userService.getUserRaking(game, name));
            verify(redisService, times(1))
                    .userExistsInLeaderboard(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Should return the correct rank")
        public void shouldReturnCorrectRank() {
            var game = "subnautica";
            var name = "ryley";

            doReturn(true)
                    .when(redisService)
                    .userExistsInLeaderboard(any(), any());

            doReturn(1L)
                    .when(redisService)
                    .getUserRaking(any(), any());

            var actualResponse = userService.getUserRaking(game, name);

            assertEquals(1L, actualResponse);
            verify(redisService, times(1))
                    .getUserRaking(any(), any());
        }

        @Test
        @DisplayName("Should pass correct parameters to redis service")
        public void shouldPassCorrectParametersToService() {
            var game = "subnautica";
            var name = "ryley";

            doReturn(true)
                    .when(redisService)
                    .userExistsInLeaderboard(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            doReturn(1L)
                    .when(redisService)
                    .getUserRaking(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            userService.getUserRaking(game, name);

            var stringsCaptured = stringArgumentCaptor.getAllValues();
            assertEquals(4, stringsCaptured.size());
            assertEquals(game, stringsCaptured.getFirst());
            assertEquals(name, stringsCaptured.get(1));
            assertEquals(game, stringsCaptured.get(2));
            assertEquals(name, stringsCaptured.get(3));

        }
    }
}