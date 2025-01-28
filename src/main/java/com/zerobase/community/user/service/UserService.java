package com.zerobase.community.user.service;

import com.zerobase.community.component.MailComponent;
import com.zerobase.community.component.SignUpMail;
import com.zerobase.community.config.TokenProvider;
import com.zerobase.community.user.dto.SignUp;
import com.zerobase.community.user.dto.VerifyCodeRequest;
import com.zerobase.community.user.entity.EmailVerification;
import com.zerobase.community.user.entity.UserEntity;
import com.zerobase.community.user.exception.CustomException;
import com.zerobase.community.user.exception.ErrorCode;
import com.zerobase.community.user.repository.EmailVerificationRepository;
import com.zerobase.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final MailComponent mailComponent;
    private final EmailVerificationRepository emailVerificationRepository;
    private final TokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginId));
    }

    public UserEntity userSignUp(SignUp form) {

        // 아이디 중복 체크
        if (userRepository.findById(form.getLoginId()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        // 비밀번호 암호화
        String encPassword = BCrypt.hashpw(form.getPassword(), BCrypt.gensalt());

        // 사용자 정보 저장
        UserEntity userEntity = UserEntity.builder()
                .loginId(form.getLoginId())
                .password(encPassword)
                .roles(List.of("ROLE_USER"))  // roles 설정
                .enabled(true)  // 인증 완료 후 활성화
                .birth(form.getBirth())
                .build();

        // 사용자 엔티티 저장 후 반환
        return userRepository.save(userEntity);
    }

    private void sendVerificationEmail(String email, String code) {
        mailComponent.sendMail(
                SignUpMail.builder()
                        .to(email)
                        .text(code)  // 인증 코드
                        .build());
    }

    // 인증 코드 검증
    public boolean sendCode(String email) {

        boolean result = false;

        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        // 인증 코드 5자리 생성
        String code = RandomStringUtils.random(
                5, true, true);

        // 인증 코드 만료 시간 (3분)
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(3);

        EmailVerification emailVerification =
                EmailVerification.builder()
                        .email(email)
                        .verificationCode(code)
                        .expiredDate(expiredDate)
                        .build();

        emailVerificationRepository.save(emailVerification);

        // 인증 이메일 발송
        sendVerificationEmail(email, code);

        return true;
    }

    public String verifyCode(VerifyCodeRequest form) {
        // 이메일로 인증 코드 조회
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 만료 시간 체크
        if (emailVerification.getExpiredDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        // 인증 코드 일치 여부 확인
        if (!emailVerification.getVerificationCode().equals(form.getCode())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 코드 삭제 (사용 후 삭제)
        emailVerificationRepository.delete(emailVerification);

        return form.getEmail();
    }

}

