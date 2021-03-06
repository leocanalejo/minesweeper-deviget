package com.lcanalejo.deviget.minesweeper.security.service;

import com.lcanalejo.deviget.minesweeper.entity.UserEntity;
import com.lcanalejo.deviget.minesweeper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (!userEntity.isPresent()) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new User(userEntity.get().getUsername(), userEntity.get().getPassword(), new ArrayList<>()); //Authorities, empty for this challenge
    }

}
