package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.service.CellService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cells")
@RequiredArgsConstructor
public class CellController {

    private final CellService cellService;

    @PatchMapping("/{cellId:\\d+}/flag")
    public Game flagCell(@PathVariable Long cellId) {
        return cellService.flagCell(cellId);
    }

    @PatchMapping("/{cellId:\\d+}/unflag")
    public Game unflagCell(@PathVariable Long cellId) {
        return cellService.unflagCell(cellId);
    }

    @PatchMapping("/{cellId:\\d+}/reveal")
    public Game revealCell(@PathVariable Long cellId) {
        return cellService.revealCell(cellId);
    }

}
