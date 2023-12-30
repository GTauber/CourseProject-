package com.ead.authuser.controller;

import static org.springframework.http.HttpStatus.OK;

import com.ead.authuser.models.entity.Response;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(OK)
    public Mono<Response<List<UserModel>>> getAllUsers() {
        return userService.findAllUsers()
            .map(users -> Response.<List<UserModel>>builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Users retrieved successfully")
                .data(Map.of("Users", users))
                .build());
    }

    @GetMapping("/{userId}")
    @ResponseStatus(OK)
    public Mono<Response<UserModel>> getUserById(@PathVariable Long userId) {
        return userService.findUserById(userId)
            .map(user -> Response.<UserModel>builder()
                .status(OK)
                .statusCode(OK.value())
                .message("User retrieved successfully")
                .data(Map.of("User", user))
                .build());
    }

}
