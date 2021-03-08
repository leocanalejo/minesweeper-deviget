package com.lcanalejo.deviget.minesweeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcanalejo.deviget.minesweeper.BaseTest;
import com.lcanalejo.deviget.minesweeper.MinesweeperApplication;
import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class)
@AutoConfigureMockMvc
public class GameControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Sql(BEFORE_TEST_SCRIPT)
    public void createGame() throws Exception {
        User user = User.builder()
                .username("username1")
                .password("password1")
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtResponse.class).getToken();

        CreateGame createGame = CreateGame.builder()
                .name("Game 1")
                .rows(10)
                .columns(10)
                .mines(10)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/games")
                .content(objectMapper.writeValueAsString(createGame))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

}
