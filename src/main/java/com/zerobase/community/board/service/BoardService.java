package com.zerobase.community.board.service;

import com.zerobase.community.board.dto.WriteForm;
import com.zerobase.community.board.entity.BoardEntity;
import com.zerobase.community.board.exception.ErrorCode;
import com.zerobase.community.board.repository.BoardRepository;
import com.zerobase.community.board.exception.CustomException;
import com.zerobase.community.user.entity.UserEntity;
import com.zerobase.community.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardEntity writeBoard(WriteForm form) {

        UserEntity user = userRepository.findByLoginId(form.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return boardRepository.save(BoardEntity.from(form, user));

    }

    public BoardEntity updateBoard(Long id, @Valid WriteForm form) {

        UserEntity user = userRepository.findByLoginId(form.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 유효성 체크 (삭제, 존재)
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));

        if (!board.getUser().getLoginId().equals(form.getLoginId())) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
        }

        return boardRepository.save(BoardEntity.from(form, user));
    }
}
