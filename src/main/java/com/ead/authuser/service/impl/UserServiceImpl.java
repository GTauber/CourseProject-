package com.ead.authuser.service.impl;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.entity.UserModel;
import com.ead.authuser.models.exceptions.EmailAlreadyExistsException;
import com.ead.authuser.models.exceptions.UserNotFoundException;
import com.ead.authuser.models.exceptions.UsernameAlreadyExistsException;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<List<UserModel>> findAllUsers() {
        return userRepository.findAll().collectList();
    }

    @Override
    public Mono<UserModel> findUserById(Long userId) {
        return userRepository.findById(userId).filter(Objects::isNull)
            .switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<UserModel> registerUser(UserDto userDto) {
        return this.existsByUsername(userDto.getUsername())
            .then(this.existsByEmail(userDto.getEmail()))
            .then(Mono.defer(() -> {
                var user = convertUserDtoToUser(userDto);
                return userRepository.save(user); // assuming save() returns Mono<User>
            }));
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

    private UserModel convertUserDtoToUser(UserDto userDto) {
        var user = new UserModel();
        BeanUtils.copyProperties(userDto, user);
        user.setUuid(UUID.randomUUID().toString());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserType(UserType.STUDENT);
        return user;
    }

}
