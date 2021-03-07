package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public Game createGame(@RequestBody @Validated CreateGame createGame) {
        return gameService.createGame(createGame);
    }

    @DeleteMapping("/{gameId:\\d+}")
    public void deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
    }

    @PatchMapping("/{gameId:\\d+}/start")
    public Game startGame(@PathVariable Long gameId) {
        return gameService.startGame(gameId);
    }

    @PatchMapping("/{gameId:\\d+}/pause")
    public Game pauseGame(@PathVariable Long gameId) {
        return gameService.pauseGame(gameId);
    }

    @GetMapping("/{gameId:\\d+}")
    public Game getGame(@PathVariable Long gameId) {
        return gameService.getGame(gameId);
    }

}
