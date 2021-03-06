package com.lcanalejo.deviget.minesweeper.dto;

import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
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

    private Long id;

    private Integer columns;

    private Integer rows;

    private GameStatus gameStatus;

    private LocalDateTime lastStartedTime;

    private Long elapsedTimeInMilliseconds;

    private List<Cell> cells;

}
