package com.lcanalejo.deviget.minesweeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcanalejo.deviget.minesweeper.BaseTest;
import com.lcanalejo.deviget.minesweeper.MinesweeperApplication;
import com.lcanalejo.deviget.minesweeper.dto.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void createUser() throws Exception {
        User user = User.builder()
                .username("new_username")
                .password("new_password")
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/users")
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}
