package com.zerobase.community.board.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDto {
    String loginId;
    List<Long> id;
}
