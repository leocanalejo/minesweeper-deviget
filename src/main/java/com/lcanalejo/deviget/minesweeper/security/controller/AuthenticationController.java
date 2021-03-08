package com.lcanalejo.deviget.minesweeper.security.controller;

import com.lcanalejo.deviget.minesweeper.exception.AuthenticationException;
import com.lcanalejo.deviget.minesweeper.security.config.JwtUtil;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import com.lcanalejo.deviget.minesweeper.security.dto.LoginRequest;
import com.lcanalejo.deviget.minesweeper.security.service.JwtUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Authentication", description = "Authentication Management API")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @ApiOperation(value = "Authenticate user to get JWT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authentication was successful and JWT was retrieved."),
            @ApiResponse(code = 400, message = "Username or password are incorrect."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtResponse authenticate(@ApiParam(name = "Login credentials", value = "Username and password", required = true) @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return JwtResponse.builder().token(token).build();
    }

}
