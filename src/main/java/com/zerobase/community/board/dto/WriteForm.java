package com.zerobase.community.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriteForm {

    /*
        NotNull : null 값 체크
        NotBlank : null, 빈 문자열, 공백 등 모두 체크
     */
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
    private String subject;

    @NotBlank
    @Size(max = 50, message = "내용은 500자 이내로 작성해주세요.")
    private String content;

    @NotBlank
    private String loginId;

    private boolean isDisplay;

    private String categoryName;

}
