package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.service.CellService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cells")
@RequiredArgsConstructor
public class CellController {

    private final CellService cellService;

    @PatchMapping(value = "/{cellId:\\d+}/flag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game flagCell(@PathVariable Long cellId) {
        return cellService.flagCell(cellId);
    }

    @PatchMapping(value = "/{cellId:\\d+}/unflag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game unflagCell(@PathVariable Long cellId) {
        return cellService.unflagCell(cellId);
    }

    @PatchMapping(value = "/{cellId:\\d+}/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game revealCell(@PathVariable Long cellId) {
        return cellService.revealCell(cellId);
    }

}
