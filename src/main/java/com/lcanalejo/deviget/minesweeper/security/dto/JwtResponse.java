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
public class JwtResponse {

    @ApiModelProperty("The JWT generated in authentication - (readonly)")
    private String token;

}
