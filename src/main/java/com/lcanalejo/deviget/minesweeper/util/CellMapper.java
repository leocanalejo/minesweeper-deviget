package com.lcanalejo.deviget.minesweeper.util;

import com.lcanalejo.deviget.minesweeper.dto.Cell;
import com.lcanalejo.deviget.minesweeper.entity.CellEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CellMapper {

    public static List<Cell> toDto(List<CellEntity> cells) {
        return cells.stream().map(CellMapper::toDto).collect(Collectors.toList());
    }

    public static Cell toDto(CellEntity cellEntity) {
        return Cell.builder()
                .id(cellEntity.getId())
                .rowPosition(cellEntity.getRowPosition())
                .columnPosition(cellEntity.getColumnPosition())
                .cellStatus(cellEntity.getCellStatus())
                .minesAround(cellEntity.getMinesAround())
                .isMine(cellEntity.getIsMine())
                .build();
    }

}
