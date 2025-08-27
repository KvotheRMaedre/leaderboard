package tech.kvothe.leaderboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import tech.kvothe.leaderboard.dto.LoginDto;
import tech.kvothe.leaderboard.dto.RecoveryJwtTokenDto;
import tech.kvothe.leaderboard.dto.UserDto;
import tech.kvothe.leaderboard.factory.LoginDtoFactory;
import tech.kvothe.leaderboard.factory.UserDtoFactory;
import tech.kvothe.leaderboard.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Captor
    private ArgumentCaptor<UserDto> userDtoArgumentCaptor;

    @Captor
    private ArgumentCaptor<LoginDto> loginDtoArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should return HttpOK")
        public void shouldReturnHttpOk() {
            var response = userController.createUser(UserDtoFactory.buildOneUserDto());

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        @DisplayName("Should pass correct parameters to service")
        public void shouldPassCorrectParametersToService() {
            var dto = UserDtoFactory.buildOneUserDto();

            doNothing()
                    .when(userService)
                    .createUser(userDtoArgumentCaptor.capture());

            var response = userController.createUser(dto);

            var dtoCaptured = userDtoArgumentCaptor.getValue();
            assertEquals(dto.name(), dtoCaptured.name());
            assertEquals(dto.userName(), dtoCaptured.userName());
            assertEquals(dto.password(), dtoCaptured.password());
        }
    }

    @Nested
    class authenticateUser {

        @Test
        @DisplayName("Should return HttpOK")
        public void shouldReturnHttpOk() {
            var token = new RecoveryJwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsZWFkZXJib2FyZCIsImlhdCI6MTc1NjMxNDYxOSwiZXhwIjoxNzU2MzI5MDE5LCJzdWIiOiJrdm90aGUifQ.oZAZ6dHdZ2UVq2ukwlgw4V2c4nJKlj3k9HhXZ1-8a9k");

            doReturn(token)
                    .when(userService)
                    .authenticateUser(loginDtoArgumentCaptor.capture());

            var response = userController.authenticateUser(LoginDtoFactory.buildOneDto());

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        @DisplayName("Should pass correct parameters to service")
        public void shouldPassCorrectParametersToService() {
            var dto = LoginDtoFactory.buildOneDto();
            var token = new RecoveryJwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsZWFkZXJib2FyZCIsImlhdCI6MTc1NjMxNDYxOSwiZXhwIjoxNzU2MzI5MDE5LCJzdWIiOiJrdm90aGUifQ.oZAZ6dHdZ2UVq2ukwlgw4V2c4nJKlj3k9HhXZ1-8a9k");

            doReturn(token)
                    .when(userService)
                    .authenticateUser(loginDtoArgumentCaptor.capture());

            var response = userController.authenticateUser(dto);

            var dtoCaptured = loginDtoArgumentCaptor.getValue();
            assertEquals(dto.userName(), dtoCaptured.userName());
            assertEquals(dto.password(), dtoCaptured.password());
        }

        @Test
        @DisplayName("Should pass correct parameters to service")
        public void shouldReturnResponseBodyCorrectly() {
            var dto = LoginDtoFactory.buildOneDto();
            var recoveryJwtTokenDto = new RecoveryJwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsZWFkZXJib2FyZCIsImlhdCI6MTc1NjMxNDYxOSwiZXhwIjoxNzU2MzI5MDE5LCJzdWIiOiJrdm90aGUifQ.oZAZ6dHdZ2UVq2ukwlgw4V2c4nJKlj3k9HhXZ1-8a9k");

            doReturn(recoveryJwtTokenDto)
                    .when(userService)
                    .authenticateUser(loginDtoArgumentCaptor.capture());

            var response = userController.authenticateUser(dto);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().token());
            assertEquals(response.getBody().token(), recoveryJwtTokenDto.token());
        }
    }
  
}