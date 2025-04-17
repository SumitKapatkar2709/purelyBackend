package com.wellNexa.userservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.wellNexa.userservice.dtos.ApiResponseDto;
import com.wellNexa.userservice.dtos.UserDto;
import com.wellNexa.userservice.exceptions.ServiceLogicException;
import com.wellNexa.userservice.exceptions.UserNotFoundException;
import com.wellNexa.userservice.modals.User;
import com.wellNexa.userservice.repositories.UserRepository;

import java.util.Objects;


@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponseDto<?>> existsUserById(String userId) throws ServiceLogicException {
        try {
            return ResponseEntity.ok(ApiResponseDto.builder()
                    .isSuccess(true)
                    .response(userRepository.existsById(userId))
                    .message("User exists.")
                    .build());
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new ServiceLogicException("Something went wrong. Try gain later!");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getUserById(String id) throws ServiceLogicException, UserNotFoundException {
        try {
            if (userRepository.existsById(id)) {
                return ResponseEntity.ok(ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(userToUserDto(Objects.requireNonNull(userRepository.findById(id).orElse(null))))
                        .build());
            }
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new ServiceLogicException("Something went wrong. Try gain later!");
        }
        throw new UserNotFoundException("User not found with id " + id);
    }

    private UserDto userToUserDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

}