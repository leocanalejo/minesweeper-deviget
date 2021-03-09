package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.service.CellService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Cell", description = "Cell Management API")
@RestController
@RequestMapping("/cells")
@RequiredArgsConstructor
@Slf4j
public class CellController {

    private final CellService cellService;

    @ApiOperation(value = "Put a flag in a cell of a game in status PLAYING")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The flag was successfully put."),
            @ApiResponse(code = 404, message = "Can't put flag because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't put flag because authenticated user is not the owner of the cell or game status is incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PutMapping(value = "/{cellId:\\d+}/flag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game flagCell(@ApiParam(name = "cellId", value = "The cell id to put a flag", required = true) @PathVariable Long cellId) {
        log.info("Putting a flag in cell with id {}", cellId);
        return cellService.flagCell(cellId);
    }

    @ApiOperation(value = "Remove a flag in a cell of a game in status PLAYING")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The flag was successfully removed."),
            @ApiResponse(code = 404, message = "Can't remove flag because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't remove flag because authenticated user is not the owner of the cell or game status is incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @DeleteMapping(value = "/{cellId:\\d+}/flag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game unflagCell(@ApiParam(name = "cellId", value = "The cell id to remove a flag", required = true) @PathVariable Long cellId) {
        log.info("Removing a flag in cell with id {}", cellId);
        return cellService.unflagCell(cellId);
    }

    @ApiOperation(value = "Reveal a cell of a game in status PLAYING")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cell was successfully revealed."),
            @ApiResponse(code = 404, message = "Can't reveal cell because it doesn't exists."),
            @ApiResponse(code = 412, message = "Can't reveal cell because authenticated user is not the owner of the cell or game status is incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PatchMapping(value = "/{cellId:\\d+}/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game revealCell(@ApiParam(name = "cellId", value = "The cell id to reveal", required = true) @PathVariable Long cellId) {
        log.info("Revealing cell with id {}", cellId);
        return cellService.revealCell(cellId);
    }

}
