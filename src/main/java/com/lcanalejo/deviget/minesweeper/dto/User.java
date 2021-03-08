package com.lcanalejo.deviget.minesweeper.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiModelProperty("The username")
    @NotNull(message = "Username cannot be null")
    @Size(min = 4, message = "Username should not have less than 4 characters.")
    private String username;

    @ApiModelProperty("The password")
    @NotNull(message = "Password cannot be null")
    @Size(min = 4, message = "Password should not have less than 4 characters.")
    private String password;

}
