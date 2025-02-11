package com.zerobase.community.board.dto;

import com.zerobase.community.board.exception.CustomException;
import com.zerobase.community.board.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Category {
    CATEGORY_1("취미"),
    CATEGORY_2("사회"),
    CATEGORY_3("운동"),
    CATEGORY_4("공부"),
    CATEGORY_5("기타");

    private final String name;

    public static Category getCategory(String name) {
        for (Category c : Category.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
    }
}
