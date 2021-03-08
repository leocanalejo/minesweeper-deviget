package com.lcanalejo.deviget.minesweeper.dto;

import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cell {

    @ApiModelProperty("The cell id - (readonly)")
    private Long id;

    @ApiModelProperty("The row position in the board - (readonly)")
    private Integer rowPosition;

    @ApiModelProperty("The column position in the board - (readonly)")
    private Integer columnPosition;

    @ApiModelProperty(value = "The cell status - (readonly)", allowableValues = "HIDDEN, REVEALED, FLAGGED")
    private CellStatus cellStatus;

    @ApiModelProperty("The number of mines around this cell - (readonly)")
    private Integer minesAround;

    @ApiModelProperty("This cell is a mine or not - (readonly)")
    private Boolean isMine;

}
