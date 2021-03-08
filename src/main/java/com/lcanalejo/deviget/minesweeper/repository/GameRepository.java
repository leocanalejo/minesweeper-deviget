package com.lcanalejo.deviget.minesweeper.repository;

import com.lcanalejo.deviget.minesweeper.entity.GameEntity;
import com.lcanalejo.deviget.minesweeper.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    Page<GameEntity> findByUser(UserEntity authenticatedUser, Pageable pageable);

}
