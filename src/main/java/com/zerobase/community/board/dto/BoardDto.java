package com.zerobase.community.board.dto;

import com.zerobase.community.board.entity.BoardEntity;
import com.zerobase.community.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private String subject;
    private String content;
    private String loginId;
    private boolean isDisplay;
    private String categoryName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public static BoardDto fromBoardEntity(BoardEntity boardEntity, UserEntity user) {

        return BoardDto.builder()
                .subject(boardEntity.getSubject())
                .content(boardEntity.getContent())
                .loginId(user.getLoginId())  // UserEntity에서 loginId 가져오기
                .isDisplay(boardEntity.isDisplay())
                .categoryName(boardEntity.getCategory().getName())  // Category Enum의 name 사용
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .build();
    }
}
