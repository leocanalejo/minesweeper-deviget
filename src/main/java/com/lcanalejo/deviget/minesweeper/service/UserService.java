package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.entity.UserEntity;
import com.lcanalejo.deviget.minesweeper.repository.UserRepository;
import com.lcanalejo.deviget.minesweeper.security.config.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createUser(User user) {
        UserEntity userEntity = UserEntity.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();
        userRepository.save(userEntity);
    }

    public UserEntity getAuthenticatedUser() {
        return SecurityUtil.getAuthenticatedUsername().flatMap(userRepository::findByUsername).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
