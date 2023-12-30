package com.ead.authuser.repository;

import com.ead.authuser.models.entity.UserModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserModel, Long> {

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

}
