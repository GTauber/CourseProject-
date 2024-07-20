package com.ead.authuser.service;

import com.ead.authuser.models.dto.UserDto;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.models.query.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Page<UserModel>> findAllUsers(Filterable filterable, Pageable pageable, Class<UserModel> entityClass);

    Mono<UserModel> findUserById(Long userId);

    Mono<UserModel> registerUser(UserDto userDto);

    Mono<Void> deleteUser(Long userId);

    Mono<UserModel> updateUser(Long userId, UserModel userDto);

    Mono<UserModel> updatePassword(Long userId, UserDto user);

    Mono<UserModel> updateImage(Long userId, UserDto userDto);
}
