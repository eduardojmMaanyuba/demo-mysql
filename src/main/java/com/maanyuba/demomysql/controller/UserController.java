package com.maanyuba.demomysql.controller;

import com.maanyuba.demomysql.dto.CreateUserRequestDto;
import com.maanyuba.demomysql.dto.GenericResponse;
import com.maanyuba.demomysql.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET, path = "/user", produces = "application/json")
    public ResponseEntity<GenericResponse> getUser(@RequestParam String username) {
        GenericResponse gr = userService.getUser(username);
        if (gr.getStatusCode() == 200) {
            return ResponseEntity.ok(gr);
        } else {
            return new ResponseEntity<>(gr, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user", produces = "application/json")
    public ResponseEntity<GenericResponse> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        GenericResponse gr = userService.createUser(createUserRequestDto);
        if (gr.getStatusCode() == 201) {
            return new ResponseEntity<>(gr, HttpStatus.CREATED);
        } else if (gr.getStatusCode() == 422) {
            return new ResponseEntity<>(gr, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(gr, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users", produces = "application/json")
    public ResponseEntity<GenericResponse> getUsers() {
        GenericResponse gr = userService.findAllUsers();
        if (gr.getStatusCode() == 200) {
            return new ResponseEntity<>(gr, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(gr, HttpStatus.NOT_FOUND);
        }
    }
}
