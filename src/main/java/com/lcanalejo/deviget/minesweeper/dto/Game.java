package com.lcanalejo.deviget.minesweeper.dto;

import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @ApiModelProperty("The game id - (readonly)")
    private Long id;

    @ApiModelProperty("The game name - (readonly)")
    private String name;

    @ApiModelProperty("The number of rows in the board - (readonly)")
    private Integer rows;

    @ApiModelProperty("The number of columns in the board - (readonly)")
    private Integer columns;

    @ApiModelProperty(value = "The game status - (readonly)", allowableValues = "CREATED, PLAYING, PAUSED, WON, LOST")
    private GameStatus gameStatus;

    @ApiModelProperty("The last time when game was started - (readonly)")
    private LocalDateTime lastStartedTime;

    @ApiModelProperty("The elapsed time in milliseconds since game was paused or be in a WON or LOST status - (readonly)")
    private Long elapsedTimeInMilliseconds;

    @ApiModelProperty("The cells in the board game - (readonly)")
    private List<Cell> cells;

}
