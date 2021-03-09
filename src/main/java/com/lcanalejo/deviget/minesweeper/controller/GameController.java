package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Game", description = "Game Management API")
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @ApiOperation(value = "Create a new game")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The game was successfully created."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@ApiParam(name = "user", value = "The game to create", required = true) @RequestBody @Validated CreateGame createGame) {
        log.info("Creating a new game with name {}", createGame.getName());
        return gameService.createGame(createGame);
    }

    @ApiOperation(value = "Delete a game")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The game was successfully deleted."),
            @ApiResponse(code = 404, message = "Can't delete game because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't delete game because authenticated user is not the owner."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @DeleteMapping(value = "/{gameId:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@ApiParam(name = "gameId", value = "The game id to delete", required = true) @PathVariable Long gameId) {
        log.info("Deleting game with id {}", gameId);
        gameService.deleteGame(gameId);
    }

    @ApiOperation(value = "Start a game in status CREATED or PAUSED")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The game was successfully started."),
            @ApiResponse(code = 404, message = "Can't start game because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't start game because authenticated user is not the owner or its status is incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PatchMapping(value = "/{gameId:\\d+}/start", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game startGame(@ApiParam(name = "gameId", value = "The game id to start", required = true) @PathVariable Long gameId) {
        log.info("Starting game with id {}", gameId);
        return gameService.startGame(gameId);
    }

    @ApiOperation(value = "Pause a game in status PLAYING")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The game was successfully paused."),
            @ApiResponse(code = 404, message = "Can't pause game because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't pause game because authenticated user is not the owner or its status is incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PatchMapping(value = "/{gameId:\\d+}/pause", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game pauseGame(@ApiParam(name = "gameId", value = "The game id to pause", required = true) @PathVariable Long gameId) {
        log.info("Pausing game with id {}", gameId);
        return gameService.pauseGame(gameId);
    }

    @ApiOperation(value = "Get a game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The game was successfully retrieved."),
            @ApiResponse(code = 404, message = "Can't retrieve game because it doesn't exists."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @GetMapping(value = "/{gameId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@ApiParam(name = "gameId", value = "The game id to retrieve", required = true) @PathVariable Long gameId) {
        log.info("Retrieving game with id {}", gameId);
        return gameService.getGame(gameId);
    }

    @ApiOperation(value = "Get games in a page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The games were successfully retrieved."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Game> getGames(@ApiParam(name = "pageable", value = "The page info to retrieve games", required = true) final Pageable pageable) {
        log.info("Retrieving games for page with page number {} and page size {}", pageable.getPageNumber(), pageable.getPageSize());
        return gameService.getGames(pageable);
    }

}
