package com.lcanalejo.deviget.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGame {

    private String name;

    private Integer rows;

    private Integer columns;

    private Integer mines;

}
