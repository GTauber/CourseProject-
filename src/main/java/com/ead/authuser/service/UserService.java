package com.ead.authuser.service;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.models.entity.UserModel;
import java.util.List;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<List<UserModel>> findAllUsers();

    Mono<UserModel> findUserById(Long userId);

    Mono<UserModel> registerUser(UserDto userDto);
}
