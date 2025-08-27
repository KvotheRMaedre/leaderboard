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

}