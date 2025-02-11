package com.zerobase.community.board.controller;

import com.zerobase.community.board.dto.WriteForm;
import com.zerobase.community.board.entity.BoardEntity;
import com.zerobase.community.board.service.BoardService;
import com.zerobase.community.security.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("hasRole('USER_WRITE')")
    @PostMapping("/write")
    public ResponseEntity<String> writeBoard(@Valid @RequestBody WriteForm form) {

        boardService.writeBoard(form);

        return ResponseEntity.ok("작성 완료");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BoardEntity> updateBoard(
            @PathVariable Long id, @Valid @RequestBody WriteForm form) {

        return ResponseEntity.ok(boardService.updateBoard(id, form));
    }

}
