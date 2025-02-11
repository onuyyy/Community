package com.zerobase.community.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "가입된 사용자가 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."),
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물입니다."),
    NOT_AUTHENTICATED(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다." );

    private final HttpStatus httpStatus;
    private final String detail;
}
