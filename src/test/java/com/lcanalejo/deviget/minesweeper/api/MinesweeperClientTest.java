package com.lcanalejo.deviget.minesweeper.api;

import com.lcanalejo.deviget.minesweeper.BaseTest;
import com.lcanalejo.deviget.minesweeper.MinesweeperApplication;
import com.lcanalejo.deviget.minesweeper.api.client.MinesweeperClient;
import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import com.lcanalejo.deviget.minesweeper.security.dto.LoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MinesweeperClientTest extends BaseTest {

    @Autowired
    private MinesweeperClient minesweeperClient;

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void createGameTest() {
        // Given
        String name = "game 1";
        Integer rows = 10;
        Integer columns = 10;
        Integer mines = 10;
        CreateGame createGame = CreateGame.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .mines(mines)
                .build();

        // When
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        Game game = minesweeperClient.createGame(createGame, jwtResponse.getToken());

        // Then
        assertEquals(name, game.getName());
        assertEquals(rows, game.getRows());
        assertEquals(columns, game.getColumns());
        assertEquals(GameStatus.CREATED, game.getGameStatus());
        assertNull(game.getLastStartedTime());
        assertEquals(new Long(0), game.getElapsedTimeInMilliseconds());
        assertEquals(rows * columns, game.getCells().size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void getGameTest() {
        // Given
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        String name = "game 1";
        Integer rows = 10;
        Integer columns = 10;
        Integer mines = 10;
        CreateGame createGame = CreateGame.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .mines(mines)
                .build();
        Game createdGame = minesweeperClient.createGame(createGame, jwtResponse.getToken());

        // When

        Game game = minesweeperClient.getGame(createdGame.getId(), jwtResponse.getToken());

        // Then
        assertEquals(createdGame.getName(), game.getName());
        assertEquals(createdGame.getRows(), game.getRows());
        assertEquals(createdGame.getColumns(), game.getColumns());
        assertEquals(createdGame.getGameStatus(), game.getGameStatus());
        assertNull(game.getLastStartedTime());
        assertEquals(new Long(0), game.getElapsedTimeInMilliseconds());
        assertEquals(createdGame.getCells().size(), game.getCells().size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void getGamesTest() {
        // When
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        Page<Game> page = minesweeperClient.getGames(PageRequest.of(0, 10), jwtResponse.getToken());

        // Then
        List<Game> games = page.getContent();
        assertFalse(games.isEmpty());
        assertEquals(3, games.size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void startGameTest() {
        // Given
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        String name = "game 1";
        Integer rows = 10;
        Integer columns = 10;
        Integer mines = 10;
        CreateGame createGame = CreateGame.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .mines(mines)
                .build();
        Game createdGame = minesweeperClient.createGame(createGame, jwtResponse.getToken());

        // When
        Game startedGame = minesweeperClient.startGame(createdGame.getId(), jwtResponse.getToken());

        // Then
        assertEquals(name, startedGame.getName());
        assertEquals(rows, startedGame.getRows());
        assertEquals(columns, startedGame.getColumns());
        assertEquals(GameStatus.PLAYING, startedGame.getGameStatus());
        assertNotNull(startedGame.getLastStartedTime());
        assertEquals(new Long(0), startedGame.getElapsedTimeInMilliseconds());
        assertEquals(rows * columns, startedGame.getCells().size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void startGameWithIncorrectStatusTest() {
        // Given
        Long gameId = 3L;

        // When
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        Exception exception = assertThrows(RestClientException.class, () -> minesweeperClient.startGame(gameId, jwtResponse.getToken()));

        // Then
        assertEquals(((HttpClientErrorException)exception.getCause()).getStatusCode(), HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void pauseGameTest() {
        // Given
        String name = "game 1";
        Integer rows = 10;
        Integer columns = 10;
        Integer mines = 10;
        CreateGame createGame = CreateGame.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .mines(mines)
                .build();
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        Game createdGame = minesweeperClient.createGame(createGame, jwtResponse.getToken());
        Game startedGame = minesweeperClient.startGame(createdGame.getId(), jwtResponse.getToken());

        // When
        Game pausedGame = minesweeperClient.pauseGame(startedGame.getId(), jwtResponse.getToken());

        // Then
        assertEquals(startedGame.getName(), pausedGame.getName());
        assertEquals(startedGame.getRows(), pausedGame.getRows());
        assertEquals(startedGame.getColumns(), pausedGame.getColumns());
        assertEquals(GameStatus.PAUSED, pausedGame.getGameStatus());
        assertNull(pausedGame.getLastStartedTime());
        assertNotNull(pausedGame.getElapsedTimeInMilliseconds());
        assertEquals(rows * columns, pausedGame.getCells().size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void pauseGameWithIncorrectStatusTest() {
        // Given
        Long gameId = 1L;

        // When
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());
        Exception exception = assertThrows(RestClientException.class, () -> minesweeperClient.pauseGame(gameId, jwtResponse.getToken()));

        // Then
        assertEquals(((HttpClientErrorException)exception.getCause()).getStatusCode(), HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void deleteGameTest() {
        // Given
        Long gameId = 1L;
        JwtResponse jwtResponse = minesweeperClient.authenticate(LoginRequest.builder().username("username1").password("password1").build());

        // When
        minesweeperClient.deleteGame(gameId, jwtResponse.getToken());

        // Then
        Exception exception = assertThrows(HttpServerErrorException.InternalServerError.class, () -> minesweeperClient.getGame(gameId, jwtResponse.getToken()));
    }

}
