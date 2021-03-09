package com.lcanalejo.deviget.minesweeper.api.config;

import com.lcanalejo.deviget.minesweeper.api.client.MinesweeperClient;
import com.lcanalejo.deviget.minesweeper.api.client.MinesweeperClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public MinesweeperClient minesweeperClient(@Value("${minesweeper.base_url}") String baseUrl) {
        return new MinesweeperClientImpl(baseUrl);
    }

}
