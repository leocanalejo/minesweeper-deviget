package com.lcanalejo.deviget.minesweeper.api.config;

import com.lcanalejo.deviget.minesweeper.api.client.MinesweeperClient;
import com.lcanalejo.deviget.minesweeper.api.client.MinesweeperClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    private final static String API_BASE_URL = "minesweeper.base_url";

    @Bean
    public MinesweeperClient minesweeperClient(@Value(API_BASE_URL) String baseUrl) {
        return new MinesweeperClientImpl(baseUrl);
    }

}
