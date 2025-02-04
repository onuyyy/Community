package com.zerobase.community.user.controller;

import com.zerobase.community.security.TokenProvider;
import com.zerobase.community.user.dto.SignUp;
import com.zerobase.community.user.dto.VerifyCodeRequest;
import com.zerobase.community.user.entity.UserEntity;
import com.zerobase.community.user.repository.UserRepository;
import com.zerobase.community.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final UserRepository userRepository;
    private final SignUpService signUpService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<UserEntity> userSignUp(@RequestBody SignUp form) {

        UserEntity user = signUpService.userSignUp(form);

        return ResponseEntity.ok(user);
    }

    /**
     * 인증 코드 전송
     * @param email
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {

        boolean isVerified = signUpService.sendCode(email);

        if (isVerified) {
            return ResponseEntity.ok("인증 번호 발송");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 번호 발송 실패");
        }
    }

    /**
     * 전송된 인증 코드 확인
     * @param form
     * @return
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest form) {

        log.info("verify code starting.. form : {}", form.getEmail());

        return ResponseEntity.ok(signUpService.verifyCode(form));
    }

}
