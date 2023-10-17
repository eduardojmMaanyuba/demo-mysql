package com.maanyuba.demomysql.service;

import com.maanyuba.demomysql.dto.CreateUserRequestDto;
import com.maanyuba.demomysql.dto.CreateUserResponseDto;
import com.maanyuba.demomysql.dto.GenericResponse;
import com.maanyuba.demomysql.entity.User;
import com.maanyuba.demomysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public GenericResponse getUser(String username) {
        try {
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found."));
            return GenericResponse.builder()
                    .statusCode(200)
                    .message("user found!.")
                    .object(CreateUserResponseDto.builder()
                            .blocked(user.isBlocked())
                            .user(user.getUsername())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponse.builder()
                    .statusCode(404)
                    .message("user no found!.")
                    .build();
        }
    }

    public GenericResponse createUser(CreateUserRequestDto createUserRequestDto) {
        try {
            if (createUserRequestDto.getUser() == null || createUserRequestDto.getUser().isEmpty()) {
                throw new RuntimeException("user is required.");
            }
            if (createUserRequestDto.getPassword() == null || createUserRequestDto.getPassword().isEmpty()) {
                throw new RuntimeException("password is required.");
            }
            if (userRepository.findUserByUsername(createUserRequestDto.getUser()).isPresent()) {
                throw new RuntimeException("user already exist.");
            }
            User user = User.builder()
                    .username(createUserRequestDto.getUser())
                    .password(passwordEncoder.encode(createUserRequestDto.getPassword()))
                    .build();
            user = userRepository.save(user);
            if (user.getId() != 0) {
                return GenericResponse.builder()
                        .object(CreateUserResponseDto.builder()
                                .blocked(user.isBlocked())
                                .user(user.getUsername())
                                .build())
                        .message("User created")
                        .statusCode(201)
                        .build();
            } else {
                return GenericResponse.builder()
                        .message("User not created")
                        .statusCode(422)
                        .build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponse.builder()
                    .message(e.getMessage())
                    .statusCode(400)
                    .build();
        }
    }

    public GenericResponse findAllUsers() {
        List<User> users = userRepository.findAll();
        List<CreateUserResponseDto> dataUsers = new ArrayList<>();
        for (User user: users) {
            dataUsers.add(CreateUserResponseDto.builder()
                    .user(user.getUsername())
                    .blocked(user.isBlocked())
                    .build());
        }
        if (dataUsers.isEmpty()) {
            return GenericResponse.builder()
                    .message("user's not found.")
                    .statusCode(404)
                    .build();
        } else {
            return GenericResponse.builder()
                    .object(dataUsers)
                    .message("user's found.")
                    .statusCode(200)
                    .build();
        }
    }
}
