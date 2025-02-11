package com.zerobase.community.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {
    private String loginId;

    @Size(min = 4, max = 20)
    private String password;
    private String email;
    private LocalDate birth;
}
