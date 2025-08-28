package tech.kvothe.leaderboard.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.kvothe.leaderboard.dto.ScoreDto;
import tech.kvothe.leaderboard.service.ScoreService;
import tech.kvothe.leaderboard.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class LeaderboardControllerTest {
    
    @Mock
    private ScoreService scoreService;
    
    @Mock
    private UserService userService;

    @Mock
    private SecurityContextHolder securityContextHolder;
    
    @InjectMocks
    private LeaderboardController leaderboardController;

    @Captor
    private ArgumentCaptor<ScoreDto> scoreDtoArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private static final String userName = "kvothe";

    @BeforeAll
    static void setUp() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userName, null, Collections.singleton(new SimpleGrantedAuthority("ADM")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @Nested
    class submitScore {
        @Test
        @DisplayName("Should return http ok")
        public void shouldReturnHttpOk() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hollowknight";

            var response = leaderboardController.submitScore(game, scoreDto);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            
        }

        @Test
        @DisplayName("Should pass correct params to service")
        public void shouldPassCorrectParamsToService() {

            var scoreDto = new ScoreDto(1.0);
            var game = "hollowknight";

            doReturn(1L)
                    .when(scoreService)
                    .submitScore(scoreDtoArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.submitScore(game, scoreDto);

            var stringCaptured = stringArgumentCaptor.getAllValues();
            assertEquals(game, stringCaptured.getFirst());
            assertEquals(userName, stringCaptured.getLast());
            assertEquals(scoreDto, scoreDtoArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("Should show correct message when a new data is submitted")
        public void shouldShowCorrectMessageWhenANewDataIsSubmitted() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hollowknight";
            var expectedMessage = "Score submitted!";

            doReturn(1L)
                    .when(scoreService)
                    .submitScore(scoreDtoArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.submitScore(game, scoreDto);

            assertEquals(expectedMessage, response.getBody().message());
        }

        @Test
        @DisplayName("Should show correct message when a data already exists")
        public void shouldShowCorrectMessageWhenDataAlreadyExists() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hollowknight";
            var expectedMessage = "We will only consider the last score you submitted for each leaderboard!";

            doReturn(0L)
                    .when(scoreService)
                    .submitScore(scoreDtoArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.submitScore(game, scoreDto);

            assertEquals(expectedMessage, response.getBody().message());
        }

        @Test
        @DisplayName("Should return correct body")
        public void shouldReturnCorrectBody() {

            var scoreDto = new ScoreDto(1.0);
            var game = "hollowknight";

            doReturn(0L)
                    .when(scoreService)
                    .submitScore(scoreDtoArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.submitScore(game, scoreDto);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().message());

        }
    }

    @Nested
    class getUserRanking {
        @Test
        @DisplayName("Should return http ok")
        public void shouldReturnHttpOk() {
            var game = "marvelrivals";

            var response = leaderboardController.getUserRanking(game);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        @DisplayName("Should pass correct params to service")
        public void shouldPassCorrectParamsToService() {
            var game = "marvelrivals";

            doReturn(1L)
                    .when(userService)
                    .getUserRaking(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.getUserRanking(game);

            var stringsCaptured = stringArgumentCaptor.getAllValues();
            assertEquals(game, stringsCaptured.getFirst());
            assertEquals(userName, stringsCaptured.getLast());
        }


        @Test
        @DisplayName("Should return correct body")
        public void shouldReturnCorrectBody() {
            var game = "marvelrivals";

            doReturn(1L)
                    .when(userService)
                    .getUserRaking(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var response = leaderboardController.getUserRanking(game);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertEquals("{ \"rank\": \"1\" }", response.getBody());
        }
    }

}