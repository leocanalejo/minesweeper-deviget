package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.entity.CellEntity;
import com.lcanalejo.deviget.minesweeper.repository.CellRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CellService {

    private final CellRepository cellRepository;

    @Setter(onMethod=@__({@Autowired}))
    private GameService gameService;

    public CellEntity save(CellEntity cellEntity) {
        return cellRepository.save(cellEntity);
    }

    public Game flagCell(Long cellId) {
        return null;
    }

    public Game unflagCell(Long cellId) {
        return null;
    }

    public Game openCell(Long cellId) {
        return null;
    }
}
