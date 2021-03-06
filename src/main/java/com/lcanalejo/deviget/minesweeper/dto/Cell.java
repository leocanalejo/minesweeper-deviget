package com.lcanalejo.deviget.minesweeper.dto;

import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cell {

    private Long id;

    private Integer rowPosition;

    private Integer columnPosition;

    private CellStatus cellStatus;

    private Integer minesAround;

    private Boolean isMine;

}
