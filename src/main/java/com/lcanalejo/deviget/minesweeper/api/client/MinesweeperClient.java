package com.lcanalejo.deviget.minesweeper.api.client;

import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.security.dto.JwtResponse;
import com.lcanalejo.deviget.minesweeper.security.dto.LoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MinesweeperClient {

    // User
    void createUser(User user);

    // Authentication
    JwtResponse authenticate(LoginRequest loginRequest);

    // Game
    Game createGame(CreateGame createGame, String jwt);
    void deleteGame(Long gameId, String jwt);
    Game getGame(Long gameId, String jwt);
    Page<Game> getGames(Pageable paginated, String jwt);
    Game startGame(Long gameId, String jwt);
    Game pauseGame(Long gameId, String jwt);

    // Cell
    Game flagCell(Long cellId, String jwt);
    Game unflagCell(Long cellId, String jwt);
    Game revealCell(Long cellId, String jwt);
}
