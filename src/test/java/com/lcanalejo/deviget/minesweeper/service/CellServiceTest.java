package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.BaseTest;
import com.lcanalejo.deviget.minesweeper.MinesweeperApplication;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.entity.CellEntity;
import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import com.lcanalejo.deviget.minesweeper.repository.CellRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class)
public class CellServiceTest extends BaseTest {

    @Autowired
    private CellService cellService;

    @Autowired
    private CellRepository cellRepository;

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void flagCellTest() {
        // Given
        Long cellId = 1L;

        // When
        cellService.flagCell(cellId);

        // Then
        CellEntity cellEntity = cellRepository.findById(cellId).get();
        assertEquals(CellStatus.FLAGGED, cellEntity.getCellStatus());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void flagRevealedCellTest() {
        // Given
        Long cellId = 3L;

        // When
        cellService.flagCell(cellId);

        // Then
        CellEntity cellEntity = cellRepository.findById(cellId).get();
        assertEquals(CellStatus.REVEALED, cellEntity.getCellStatus());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void revealMineTest() {
        // Given
        Long cellId = 9L;

        // When
        Game game = cellService.revealCell(cellId);

        // Then
        assertEquals(GameStatus.LOST, game.getGameStatus());
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void revealCellTest() {
        // Given
        Long cellId = 1L;

        // When
        Game game = cellService.revealCell(cellId);

        // Then
        long revealedCells = game.getCells().stream()
                .filter(cell -> CellStatus.REVEALED.equals(cell.getCellStatus())).count();
        assertEquals(5, revealedCells);
    }

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void revealCellAndWonTest() {
        // Given
        cellService.revealCell(1L);
        cellService.revealCell(5L);
        cellService.revealCell(6L);

        // When
        Game game = cellService.revealCell(8L);

        // Then
        assertEquals(GameStatus.WON, game.getGameStatus());
    }

}
