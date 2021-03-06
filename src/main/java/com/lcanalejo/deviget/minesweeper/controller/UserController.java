package com.lcanalejo.deviget.minesweeper.controller;

import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void register(@RequestBody User user) {
        userService.createUser(user);
    }

}
