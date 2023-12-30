package com.ead.authuser.controller;


import static org.springframework.http.HttpStatus.CREATED;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.models.entity.Response;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.service.UserService;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthenticatorController {

    private final UserService userService;

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<Response<UserModel>> registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto)
            .map(user -> Response.<UserModel>builder()
                .status(CREATED)
                .statusCode(CREATED.value())
                .message("User created successfully")
                .data(Map.of("User", user))
                .build());
    }

}
