package com.zerobase.community.board.entity;

import com.zerobase.community.board.dto.Category;
import com.zerobase.community.board.dto.WriteForm;
import com.zerobase.community.user.entity.BaseEntity;
import com.zerobase.community.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "Board")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "제목은 필수로 입력해 주세요.")
    private String subject;

    @NotNull(message = "본문 내용이 없습니다.")
    private String content;

    private boolean isDisplay;

    // 한 개의 UserEntity에 여러 개의 BoardEntity가 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", referencedColumnName = "loginId", nullable = false)
    private UserEntity user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    public static BoardEntity from(WriteForm form, UserEntity user) {
        // WriteForm에서 categoryName을 받아 Category enum으로 변환
        Category category = Category.getCategory(form.getCategoryName());

        return BoardEntity.builder()
                .subject(form.getSubject())
                .content(form.getContent())
                .user(user)
                .isDisplay(form.isDisplay())
                .category(category)
                .build();
    }

}
