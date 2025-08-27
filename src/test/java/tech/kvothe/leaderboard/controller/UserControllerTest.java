package tech.kvothe.leaderboard.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
import tech.kvothe.leaderboard.dto.UserDto;
import tech.kvothe.leaderboard.factory.UserDtoFactory;
import tech.kvothe.leaderboard.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Captor
    private ArgumentCaptor<UserDto> userDtoArgumentCaptor;

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
  
}