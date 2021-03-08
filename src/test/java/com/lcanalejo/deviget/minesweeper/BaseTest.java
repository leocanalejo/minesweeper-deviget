package com.lcanalejo.deviget.minesweeper;

import com.lcanalejo.deviget.minesweeper.entity.UserEntity;
import com.lcanalejo.deviget.minesweeper.service.UserService;
import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

public class BaseTest {

    @MockBean
    protected UserService userService;

    protected final String BEFORE_TEST_SCRIPT = "classpath:db/beforeTest.sql";

    @Before
    public void init() {
        UserEntity user = UserEntity.builder()
                .id(99L)
                .username("username1")
                .password("password1")
                .build();
        when(userService.getAuthenticatedUser()).thenReturn(user);
    }

}

