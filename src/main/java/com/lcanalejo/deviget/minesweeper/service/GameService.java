package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.dto.CreateGame;
import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.entity.CellEntity;
import com.lcanalejo.deviget.minesweeper.entity.GameEntity;
import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import com.lcanalejo.deviget.minesweeper.exception.PreconditionException;
import com.lcanalejo.deviget.minesweeper.repository.GameRepository;
import com.lcanalejo.deviget.minesweeper.util.GameMapper;
import com.lcanalejo.deviget.minesweeper.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    @Setter(onMethod=@__({@Autowired}))
    private CellService cellService;

    @Transactional
    public Game createGame(CreateGame createGame) {
        GameEntity gameEntity = GameEntity.builder()
                .name(createGame.getName())
                .columns(createGame.getColumns())
                .rows(createGame.getRows())
                .gameStatus(GameStatus.CREATED)
                .user(userService.getAuthenticatedUser())
                .elapsedTimeInMilliseconds(0L)
                .build();
        GameEntity savedGameEntity = gameRepository.save(gameEntity);

        List<CellEntity> board = createBoard(createGame.getColumns(), createGame.getRows(), createGame.getMines(), savedGameEntity);
        savedGameEntity.setCells(board);

        return GameMapper.toDto(gameRepository.save(savedGameEntity));
    }

    @Transactional(readOnly = true)
    public Game getGame(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Game with id %s not found.", gameId)));
        return GameMapper.toDto(gameEntity);
    }

    @Transactional(readOnly = true)
    public Page<Game> getGames(Pageable pageable) {
        Page<GameEntity> games = gameRepository.findByUser(userService.getAuthenticatedUser(), pageable);
        return new PageImpl<>(games.stream().map(GameMapper::toDto).collect(Collectors.toList()));
    }

    @Transactional
    public Game startGame(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Game with id %s not found.", gameId)));

        if (!userService.getAuthenticatedUser().getId().equals(gameEntity.getUser().getId())) {
            throw new PreconditionException(String.format("Authenticated user is not the owner of game with id %s", gameId));
        }

        if (Arrays.asList(GameStatus.LOST, GameStatus.WON, GameStatus.PLAYING).contains(gameEntity.getGameStatus())) {
            throw new PreconditionException(String.format("Can not start game %s with status %s", gameId, gameEntity.getGameStatus()));
        }

        gameEntity.setLastStartedTime(LocalDateTime.now());
        gameEntity.setGameStatus(GameStatus.PLAYING);
        GameEntity savedGameEntity = gameRepository.save(gameEntity);

        return GameMapper.toDto(gameRepository.save(savedGameEntity));
    }

    @Transactional
    public Game pauseGame(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Game with id %s not found.", gameId)));

        if (!userService.getAuthenticatedUser().getId().equals(gameEntity.getUser().getId())) {
            throw new PreconditionException(String.format("Authenticated user is not the owner of game with id %s", gameId));
        }

        if (!GameStatus.PLAYING.equals(gameEntity.getGameStatus())) {
            throw new PreconditionException(String.format("Can not pause game %s with status %s", gameId, gameEntity.getGameStatus()));
        }

        gameEntity.setElapsedTimeInMilliseconds((gameEntity.getElapsedTimeInMilliseconds() != null ? gameEntity.getElapsedTimeInMilliseconds() : 0) + (gameEntity.getLastStartedTime() != null ? gameEntity.getLastStartedTime().until(LocalDateTime.now(), ChronoUnit.MILLIS) : 0));
        gameEntity.setLastStartedTime(null);
        gameEntity.setGameStatus(GameStatus.PAUSED);

        return GameMapper.toDto(gameRepository.save(gameEntity));
    }

    @Transactional
    public Game saveGame(GameEntity gameEntity) {
        return GameMapper.toDto(gameRepository.save(gameEntity));
    }

    @Transactional
    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    @Transactional
    protected List<CellEntity> createBoard(Integer rows, Integer columns, Integer mines, GameEntity gameEntity) {

        final int MINE = 99;

        List<CellEntity> cellEntities = new ArrayList<>();
        int[][] board = new int[columns][rows];

        for (int r = 0; r < columns; r++) {
            for (int c = 0; c < rows; c++) {
                board[r][c] = 0;
            }
        }

        for (int m = 0; m < mines; m++) {
            // Search a random position to put the mine, where another mine is not placed
            int r, c;
            do {
                r = (int)(Math.random() * rows);
                c = (int)(Math.random() * columns);
            } while (board[r][c] == MINE); // 99 is a mine

            // Put the mine
            board[r][c] = MINE;

            // Iterate the mine's contour to increase the minesAround value
            for (int r2 = MathUtil.max(0, r-1); r2 < MathUtil.min(rows,r+2); r2++) {
                for (int c2 = MathUtil.max(0,c-1); c2 < MathUtil.min(columns,c+2); c2++) {
                    if (board[r2][c2] != MINE ) { // If it's not a mine
                        board[r2][c2]++; // Increase the mineAround value
                    }
                }
            }
        }

        for (int r = 0; r < columns; r++) {
            for (int c = 0; c < rows; c++) {
                CellEntity cellEntity = CellEntity.builder()
                        .rowPosition(r)
                        .columnPosition(c)
                        .cellStatus(CellStatus.HIDDEN)
                        .minesAround(board[r][c] != MINE ? board[r][c] : null)
                        .isMine(board[r][c] == MINE)
                        .game(gameEntity)
                        .build();
                CellEntity savedCellEntity = cellService.save(cellEntity);
                cellEntities.add(savedCellEntity);
            }
        }

        return cellEntities;
    }

}
