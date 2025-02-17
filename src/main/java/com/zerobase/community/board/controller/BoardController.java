package com.zerobase.community.board.controller;

import com.zerobase.community.board.dto.BoardDto;
import com.zerobase.community.board.dto.DeleteDto;
import com.zerobase.community.board.dto.WriteForm;
import com.zerobase.community.board.entity.BoardEntity;
import com.zerobase.community.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     * @param form
     * @return
     */
    @PreAuthorize("hasRole('USER_WRITE')")
    @PostMapping("/write")
    public ResponseEntity<String> writeBoard(@Valid @RequestBody WriteForm form) {

        boardService.writeBoard(form);

        return ResponseEntity.ok("작성 완료");
    }

    /**
     * 게시글 수정
     * @param id
     * @param form
     * @return
     */
    @PreAuthorize("hasRole('USER_UPDATE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<BoardEntity> updateBoard(
            @PathVariable Long id, @Valid @RequestBody WriteForm form) {

        return ResponseEntity.ok(boardService.updateBoard(id, form));
    }

    /**
     * 게시글 삭제
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('USER_DELETE')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBoard(@RequestBody DeleteDto dto) {

        boardService.deleteBoard(dto);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<Page<BoardDto>> getBoardList(
            @PageableDefault(page = 0, size = 10, sort = "createDate") Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.getBoardList(pageable));
    }

}
