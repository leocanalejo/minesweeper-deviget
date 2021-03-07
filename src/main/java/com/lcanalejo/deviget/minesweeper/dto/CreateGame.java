package com.lcanalejo.deviget.minesweeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGame {

    private String name;

    private Integer rows;

    private Integer columns;

    private Integer mines;

}
