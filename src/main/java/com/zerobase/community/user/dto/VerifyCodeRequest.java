package com.zerobase.community.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyCodeRequest {

    private String email;  // 사용자 이메일
    private String code;   // 입력된 인증 코드
}
