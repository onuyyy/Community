package com.zerobase.community.board.exception;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }

    public CustomException(List<Long> notFoundIds) {
        super(ErrorCode.getNotFoundBoardDetail(notFoundIds));
        this.errorCode = ErrorCode.NOT_FOUND_BOARD;
    }
}
