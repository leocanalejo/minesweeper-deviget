package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "User", description = "User Management API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user was successfully created."),
            @ApiResponse(code = 400, message = "Username is already in use."),
            @ApiResponse(code = 500, message = "Internal server error.")}
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@ApiParam(name = "user", value = "The user to create", required = true) @RequestBody @Validated User user) {
        log.info("Creating user {}", user);
        userService.createUser(user);
    }

}
