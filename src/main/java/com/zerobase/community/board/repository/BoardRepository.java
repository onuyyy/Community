package com.zerobase.community.board.repository;

import com.zerobase.community.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    Page<BoardEntity> findAllByIsDisplayAndDeleteDateIsNull(
            boolean isDisplay, Pageable pageable);
}

