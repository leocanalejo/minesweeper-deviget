package com.lcanalejo.deviget.minesweeper.service;

import com.lcanalejo.deviget.minesweeper.dto.User;
import com.lcanalejo.deviget.minesweeper.entity.UserAccountEntity;
import com.lcanalejo.deviget.minesweeper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createUser(User user) {
        UserAccountEntity userAccountEntity = UserAccountEntity.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();
        userRepository.save(userAccountEntity);
    }

}
