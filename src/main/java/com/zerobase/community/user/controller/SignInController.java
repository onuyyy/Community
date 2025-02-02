package com.zerobase.community.user.controller;

import com.zerobase.community.security.TokenProvider;
import com.zerobase.community.user.dto.SignIn;
import com.zerobase.community.user.service.SignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/signin")
public class SignInController {

    private final TokenProvider tokenProvider;
    private final SignInService signInService;

    @PostMapping
    public ResponseEntity<String> signIn(@RequestBody SignIn signIn) {

        log.info("signIn: {}", signIn.getLoginId());

        var user = signInService.signIn(signIn);
        var token = tokenProvider.generateToken(user.getLoginId(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('ROLE_USER_READ')")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("test");

        return ResponseEntity.ok("test completed");
    }

}
