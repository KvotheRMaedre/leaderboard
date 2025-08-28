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
import tech.kvothe.leaderboard.dto.ScoreDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private ScoreService scoreService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Double> doubleArgumentCaptor;

    @Nested
    class submitScore {

        @Test
        @DisplayName("Should return correct value when the data did not exists")
        public void shouldReturnCorrectValueWhenDataDidntExists() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hadesII";
            var userName = "zagreus";

            doReturn(1L)
                    .when(redisService)
                    .addScore(stringArgumentCaptor.capture(), doubleArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var actualResponse = scoreService.submitScore(scoreDto, game, userName);

            assertEquals(1L, actualResponse);
        }

        @Test
        @DisplayName("Should return correct value when the data already exists")
        public void shouldReturnCorrectValueWhenDataAlreadyExists() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hadesII";
            var userName = "zagreus";

            doReturn(0L)
                    .when(redisService)
                    .addScore(stringArgumentCaptor.capture(), doubleArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var actualResponse = scoreService.submitScore(scoreDto, game, userName);

            assertEquals(0L, actualResponse);
        }

        @Test
        @DisplayName("Should pass the correct parameters to the service")
        public void shouldPassCorrectParametersToService() {
            var scoreDto = new ScoreDto(1.0);
            var game = "hadesII";
            var userName = "zagreus";

            doReturn(0L)
                    .when(redisService)
                    .addScore(stringArgumentCaptor.capture(), doubleArgumentCaptor.capture(), stringArgumentCaptor.capture());

            var actualResponse = scoreService.submitScore(scoreDto, game, userName);

            var stringsCaptured = stringArgumentCaptor.getAllValues();
            var scoreCaptured = doubleArgumentCaptor.getValue();

            assertEquals(game, stringsCaptured.getFirst());
            assertEquals(scoreDto.score(), scoreCaptured);
            assertEquals(userName, stringsCaptured.getLast());
        }
    }
}