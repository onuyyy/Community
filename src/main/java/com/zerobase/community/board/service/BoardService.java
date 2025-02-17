package com.zerobase.community.board.service;

import com.zerobase.community.board.dto.BoardDto;
import com.zerobase.community.board.dto.DeleteDto;
import com.zerobase.community.board.dto.WriteForm;
import com.zerobase.community.board.entity.BoardEntity;
import com.zerobase.community.board.exception.ErrorCode;
import com.zerobase.community.board.repository.BoardRepository;
import com.zerobase.community.board.exception.CustomException;
import com.zerobase.community.user.entity.UserEntity;
import com.zerobase.community.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public void writeBoard(WriteForm form) {

        UserEntity user = userRepository.findByLoginId(form.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        boardRepository.save(BoardEntity.from(form, user));

    }

    @Transactional
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

    @Transactional
    public void deleteBoard(DeleteDto dto) {

        List<Long> ids = dto.getId();
        List<BoardEntity> boards = boardRepository.findAllById(ids);

        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        List<Long> notExistIds = ids.stream()
                        .filter(id -> boards.stream().noneMatch(board
                                -> Long.valueOf(board.getId()).equals(id)))
                                .collect(Collectors.toList());

        if (!notExistIds.isEmpty()) {
            throw new CustomException(notExistIds);
        }

        boardRepository.deleteAll(boards);
    }

    @Transactional
    public Page<BoardDto> getBoardList(Pageable pageable) {

        Page<BoardEntity> list = boardRepository.findAllByIsDisplayAndDeleteDateIsNull(true, pageable);

        Page<BoardDto> boardDtos = list.map(entity -> {
            UserEntity user = entity.getUser();
            return BoardDto.fromBoardEntity(entity, user);
        });

        return boardDtos;
    }
}
