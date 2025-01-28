package com.zerobase.community.component;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data
public class SignUpMail {
    private String from;
    private String to;
    private String subject;
    private String text;
}
