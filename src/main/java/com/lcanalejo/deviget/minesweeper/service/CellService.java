package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.entity.CellEntity;
import com.lcanalejo.deviget.minesweeper.entity.GameEntity;
import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import com.lcanalejo.deviget.minesweeper.exception.PreconditionException;
import com.lcanalejo.deviget.minesweeper.repository.CellRepository;
import com.lcanalejo.deviget.minesweeper.util.GameMapper;
import com.lcanalejo.deviget.minesweeper.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CellService {

    private final CellRepository cellRepository;
    private final UserService userService;

    @Setter(onMethod=@__({@Autowired}))
    private GameService gameService;

    @Transactional
    public CellEntity save(CellEntity cellEntity) {
        return cellRepository.save(cellEntity);
    }

    @Transactional
    public Game flagCell(Long cellId) {
        CellEntity cellEntity = cellRepository.findById(cellId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cell with id %s not found.", cellId)));

        if (!userService.getAuthenticatedUser().getId().equals(cellEntity.getGame().getUser().getId())) {
            throw new PreconditionException(String.format("Authenticated user is not the owner of cell with id %s", cellId));
        }

        if (!GameStatus.PLAYING.equals(cellEntity.getGame().getGameStatus())) {
            throw new PreconditionException(String.format("Can not flag cell of game %s with status %s", cellEntity.getGame().getId(), cellEntity.getGame().getGameStatus()));
        }

        if (!CellStatus.REVEALED.equals(cellEntity.getCellStatus())) {
            cellEntity.setCellStatus(CellStatus.FLAGGED);
            cellRepository.saveAndFlush(cellEntity);
        }

        return gameService.getGame(cellEntity.getGame().getId());
    }

    @Transactional
    public Game unflagCell(Long cellId) {
        CellEntity cellEntity = cellRepository.findById(cellId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cell with id %s not found.", cellId)));

        if (!userService.getAuthenticatedUser().getId().equals(cellEntity.getGame().getUser().getId())) {
            throw new PreconditionException(String.format("Authenticated user is not the owner of cell with id %s", cellId));
        }

        if (!GameStatus.PLAYING.equals(cellEntity.getGame().getGameStatus())) {
            throw new PreconditionException(String.format("Can not flag cell of game %s with status %s", cellEntity.getGame().getId(), cellEntity.getGame().getGameStatus()));
        }

        if (CellStatus.FLAGGED.equals(cellEntity.getCellStatus())) {
            cellEntity.setCellStatus(CellStatus.HIDDEN);
            cellRepository.saveAndFlush(cellEntity);
        }

        return gameService.getGame(cellEntity.getGame().getId());
    }

    @Transactional
    public Game revealCell(Long cellId) {
        CellEntity cellEntity = cellRepository.findById(cellId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cell with id %s not found.", cellId)));

        if (!userService.getAuthenticatedUser().getId().equals(cellEntity.getGame().getUser().getId())) {
            throw new PreconditionException(String.format("Authenticated user is not the owner of cell with id %s", cellId));
        }

        if (!GameStatus.PLAYING.equals(cellEntity.getGame().getGameStatus())) {
            throw new PreconditionException(String.format("Can not reveal cell of game %s with status %s", cellEntity.getGame().getId(), cellEntity.getGame().getGameStatus()));
        }

        if (cellEntity.getIsMine()) {
            GameEntity gameEntity = cellEntity.getGame();
            gameEntity.setGameStatus(GameStatus.LOST);
            gameEntity.setElapsedTimeInMilliseconds(gameEntity.getElapsedTimeInMilliseconds() + (gameEntity.getLastStartedTime() != null ? gameEntity.getLastStartedTime().until(LocalDateTime.now(), ChronoUnit.MILLIS) : 0));
            gameEntity.setLastStartedTime(null);
            return gameService.saveGame(gameEntity);
        }

        revealCells(cellEntity);

        return GameMapper.toDto(cellEntity.getGame());
    }

    private void revealCells(CellEntity cellEntity) {
        if (!CellStatus.REVEALED.equals(cellEntity.getCellStatus())) {

            cellEntity.setCellStatus(CellStatus.REVEALED);
            cellRepository.save(cellEntity);

            if (wonGame(cellEntity.getGame())) {
                GameEntity gameEntity = cellEntity.getGame();
                gameEntity.setGameStatus(GameStatus.WON);
                gameEntity.setElapsedTimeInMilliseconds(gameEntity.getElapsedTimeInMilliseconds() + (gameEntity.getLastStartedTime() != null ? gameEntity.getLastStartedTime().until(LocalDateTime.now(), ChronoUnit.MILLIS) : 0));
                gameEntity.setLastStartedTime(null);
                return;
            }

            for (int r = MathUtil.max(0, cellEntity.getRowPosition() - 1); r < MathUtil.min(cellEntity.getGame().getRows(), cellEntity.getRowPosition() + 2); r++) {
                for (int c = MathUtil.max(0, cellEntity.getColumnPosition() - 1); c < MathUtil.min(cellEntity.getGame().getColumns(), cellEntity.getColumnPosition() + 2); c++) {
                    CellEntity nextCellEntity = cellRepository.findByRowPositionAndColumnPositionAndGame(r, c, cellEntity.getGame());
                    if (nextCellEntity.getMinesAround() != null && nextCellEntity.getMinesAround() == 0) {
                        revealCells(nextCellEntity);
                    }
                }
            }
        }
    }

    private boolean wonGame(GameEntity game) {
        return game.getCells().stream()
                .allMatch(cell -> cell.getIsMine() || CellStatus.REVEALED.equals(cell.getCellStatus()));
    }
}
