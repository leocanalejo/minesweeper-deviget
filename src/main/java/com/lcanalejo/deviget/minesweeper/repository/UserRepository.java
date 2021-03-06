package com.lcanalejo.deviget.minesweeper.repository;

import com.lcanalejo.deviget.minesweeper.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccountEntity, Long> {

    Optional<UserAccountEntity> findByUsername(String username);

}
