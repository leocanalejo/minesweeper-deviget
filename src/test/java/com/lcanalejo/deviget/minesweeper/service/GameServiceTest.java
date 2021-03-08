package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.BaseTest;
import com.lcanalejo.deviget.minesweeper.MinesweeperApplication;
import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import com.lcanalejo.deviget.minesweeper.exception.PreconditionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class)
public class GameServiceTest extends BaseTest {

    @Autowired
    private GameService gameService;

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
        Game game = gameService.createGame(createGame);

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
        Game createdGame = gameService.createGame(createGame);

        // When

        Game game = gameService.getGame(createdGame.getId());

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
        Page<Game> page = gameService.getGames(PageRequest.of(0, 10));

        // Then
        List<Game> games = page.getContent();
        assertFalse(games.isEmpty());
        assertEquals(3, games.size());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void startGameTest() {
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
        Game createdGame = gameService.createGame(createGame);

        // When
        Game startedGame = gameService.startGame(createdGame.getId());

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
        Exception exception = assertThrows(PreconditionException.class, () -> gameService.startGame(gameId));

        // Then
        assertEquals(exception.getMessage(), String.format("Can not start game %s with status %s", gameId, GameStatus.LOST));
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
        Game createdGame = gameService.createGame(createGame);
        Game startedGame = gameService.startGame(createdGame.getId());

        // When
        Game pausedGame = gameService.pauseGame(startedGame.getId());

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
        Exception exception = assertThrows(PreconditionException.class, () -> gameService.pauseGame(gameId));

        // Then
        assertEquals(exception.getMessage(), String.format("Can not pause game %s with status %s", gameId, GameStatus.CREATED));
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void deleteGameTest() {
        // Given
        Long gameId = 1L;

        // When
        gameService.deleteGame(gameId);

        // Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> gameService.getGame(gameId));
        assertEquals(exception.getMessage(), String.format("Game with id %s not found.", gameId));
    }

}
