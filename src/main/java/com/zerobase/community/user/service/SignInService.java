package com.zerobase.community.user.service;

import com.zerobase.community.user.dto.SignIn;
import com.zerobase.community.user.entity.UserEntity;
import com.zerobase.community.user.exception.CustomException;
import com.zerobase.community.user.exception.ErrorCode;
import com.zerobase.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final UserRepository userRepository;

    public UserEntity signIn(SignIn signIn) {

        UserEntity user = userRepository.findByLoginId(signIn.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!BCrypt.checkpw(signIn.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }

        return user;
    }

}
