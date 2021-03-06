package com.lcanalejo.deviget.minesweeper.security.controller;

import com.lcanalejo.deviget.minesweeper.exception.AuthenticationException;
import com.lcanalejo.deviget.minesweeper.security.config.JwtUtil;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import com.lcanalejo.deviget.minesweeper.security.dto.LoginRequest;
import com.lcanalejo.deviget.minesweeper.security.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/authenticate")
    public JwtResponse authenticate(@RequestBody LoginRequest loginRequest) {
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
