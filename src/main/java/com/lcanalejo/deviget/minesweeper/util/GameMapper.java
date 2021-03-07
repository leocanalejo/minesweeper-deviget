package com.lcanalejo.deviget.minesweeper.util;

import com.lcanalejo.deviget.minesweeper.dto.Game;
import com.lcanalejo.deviget.minesweeper.entity.GameEntity;

public final class GameMapper {

    public static Game toDto(GameEntity gameEntity) {
        return Game.builder()
                .id(gameEntity.getId())
                .name(gameEntity.getName())
                .rows(gameEntity.getRows())
                .columns(gameEntity.getColumns())
                .gameStatus(gameEntity.getGameStatus())
                .lastStartedTime(gameEntity.getLastStartedTime())
                .elapsedTimeInMilliseconds(gameEntity.getElapsedTimeInMilliseconds())
                .cells(CellMapper.toDto(gameEntity.getCells()))
                .build();
    }

}
