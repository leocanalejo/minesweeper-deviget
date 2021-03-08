package com.lcanalejo.deviget.minesweeper.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGame {

    @ApiModelProperty("The game name")
    @NotNull(message = "Name cannot be null")
    @Size(min = 4, message = "Game name should not have less than 4 characters.")
    private String name;

    @ApiModelProperty("The number of rows in the board")
    @NotNull(message = "Rows cannot be null")
    @Min(value = 3, message = "Rows should not be less than 3.")
    private Integer rows;

    @ApiModelProperty("The number of columns in the board")
    @NotNull(message = "Columns cannot be null")
    @Min(value = 3, message = "Columns should not be less than 3.")
    private Integer columns;

    @ApiModelProperty("The number of mines in the board")
    @NotNull(message = "Mines cannot be null")
    @Min(value = 1, message = "Mine should not be less than 1.")
    private Integer mines;

}
