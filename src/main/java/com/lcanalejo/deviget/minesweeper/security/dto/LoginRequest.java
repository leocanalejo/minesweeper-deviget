package com.lcanalejo.deviget.minesweeper.security.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @ApiModelProperty("The username of user authenticating - (readonly)")
    private String username;

    @ApiModelProperty("The password of user authenticating - (readonly)")
    private String password;

}
