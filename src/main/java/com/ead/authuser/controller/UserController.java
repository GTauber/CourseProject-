package com.ead.authuser.controller;

import static org.springframework.http.HttpStatus.OK;

import com.ead.authuser.mapper.UserMapper;
import com.ead.authuser.models.dto.UserDto;
import com.ead.authuser.models.dto.UserDto.UserView.ImagePut;
import com.ead.authuser.models.dto.UserDto.UserView.PasswordPut;
import com.ead.authuser.models.dto.UserDto.UserView.UserPut;
import com.ead.authuser.models.entity.Response;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.models.query.filter.UserFilterable;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(OK)
    public Mono<ResponseEntity<Page<UserModel>>> getAllUsers(UserFilterable userFilterable, Pageable page) {
        log.info("Request received: Get all users");

        return userService.findAllUsers(userFilterable,
                page, UserModel.class)
            .map(pages -> ResponseEntity.ok().body(pages));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(OK)
    public Mono<Response<UserModel>> getUserById(@PathVariable Long userId) {
        log.info("Request received: Get user with id: {}", userId);
        return userService.findUserById(userId)
            .map(user -> Response.<UserModel>builder()
                .status(OK)
                .statusCode(OK.value())
                .message("User retrieved successfully")
                .data(Map.of("User", user))
                .build());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(OK)
    public Mono<Void> deleteUserById(@PathVariable Long userId) {
        log.info("Request received: Delete user with id: {}", userId);
        return userService.deleteUser(userId)
            .then();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(OK)
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable Long userId,
        @RequestBody @Validated(UserPut.class) @JsonView(UserPut.class) UserDto userDto) {
        log.info("Request received: Update user with id: {}", userId);
        var user = userMapper.toEntity(userDto);
        return userService.updateUser(userId, user)
            .map(userMapper::toDto)
            .map(ResponseEntity::ok);
    }


    @PutMapping("/{userId}/password")
    @ResponseStatus(OK)
    public Mono<ResponseEntity<UserDto>> updatePassword(@PathVariable Long userId,
        @RequestBody @Validated(PasswordPut.class) @JsonView(PasswordPut.class) UserDto userDto) {
        log.info("Request received: Update password for user with id: {}", userId);
        return userService.updatePassword(userId, userDto)
            .map(userMapper::toDto)
            .map(ResponseEntity::ok);
    }

    @PutMapping("/{userId}/image")
    @ResponseStatus(OK)
    public Mono<ResponseEntity<UserDto>> updateImage(@PathVariable Long userId,
        @RequestBody @Validated(ImagePut.class) @JsonView(ImagePut.class) UserDto userDto) {
        log.info("Request received: Update image for user with id: {}", userId);
        return userService.updateImage(userId, userDto)
            .map(userMapper::toDto)
            .map(ResponseEntity::ok);
    }

}
