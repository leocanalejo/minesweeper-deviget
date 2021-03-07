package com.lcanalejo.deviget.minesweeper.repository;

import com.lcanalejo.deviget.minesweeper.entity.CellEntity;
import com.lcanalejo.deviget.minesweeper.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CellRepository extends JpaRepository<CellEntity, Long> {

    CellEntity findByRowPositionAndColumnPositionAndGame(Integer row, Integer column, GameEntity game);

}
