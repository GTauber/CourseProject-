package com.ead.authuser.service.impl;

import com.ead.authuser.enums.UserType;
import com.ead.authuser.mapper.UserMapper;
import com.ead.authuser.models.dto.UserDto;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.models.exceptions.EmailAlreadyExistsException;
import com.ead.authuser.models.exceptions.MismatchedOldPasswordException;
import com.ead.authuser.models.exceptions.UserNotFoundException;
import com.ead.authuser.models.exceptions.UsernameAlreadyExistsException;
import com.ead.authuser.models.query.Filterable;
import com.ead.authuser.repository.GenericFilteringRepository;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GenericFilteringRepository genericFilteringRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<Page<UserModel>> findAllUsers(Filterable filterable, Pageable pageable, Class<UserModel> entityClass) {
        return genericFilteringRepository.findAllWithFilter(filterable, pageable, entityClass);
    }

    @Override
    public Mono<UserModel> findUserById(Long userId) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<UserModel> registerUser(UserDto userDto) {
        return this.existsByUsername(userDto.username())
            .then(this.existsByEmail(userDto.email()))
            .then(Mono.defer(() -> {
                var user = userMapper.toEntity(userDto);
                user.setUserType(UserType.STUDENT);
                return userRepository.save(user);
            }));
    }

    @Override
    public Mono<Void> deleteUser(Long userId) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(UserNotFoundException::new))
            .flatMap(userRepository::delete)
            .then();
    }

    @Override
    public Mono<UserModel> updateUser(Long userId, UserModel updatedUser) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(UserNotFoundException::new))
            .doOnNext(user -> log.info("[Update Request] Updating user id: {}, User [{}]", user.getId(), user))
            .doOnNext(user -> userMapper.partialUpdate(updatedUser, user))
            .flatMap(userRepository::save);
    }

    @Override
    public Mono<UserModel> updatePassword(Long userId, UserDto userDto) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(UserNotFoundException::new))
            .doOnNext(user -> log.info("[Update Password Request] Updating password for user id: {}", user.getId()))
            .filter(user -> user.getPassword().equals(userDto.oldPassword()))
            .switchIfEmpty(Mono.error(MismatchedOldPasswordException::new))
            .doOnNext(user -> user.setPassword(userDto.password()))
            .flatMap(userRepository::save);
    }

    @Override
    public Mono<UserModel> updateImage(Long userId, UserDto userDto) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(UserNotFoundException::new))
            .doOnNext(user -> log.info("[Update Image Request] Updating image for user id: {}", user.getId()))
            .doOnNext(user -> user.setImageUrl(userDto.imageUrl()))
            .flatMap(userRepository::save);
    }

    private Mono<Void> existsByUsername(String username) {
        return userRepository.existsByUsername(username)
            .filter(bol -> !bol)
            .switchIfEmpty(Mono.error(UsernameAlreadyExistsException::new))
            .then();
    }

    private Mono<Void> existsByEmail(String email) {
        return userRepository.existsByEmail(email)
            .filter(bol -> !bol)
            .switchIfEmpty(Mono.error(EmailAlreadyExistsException::new))
            .then();
    }
}
