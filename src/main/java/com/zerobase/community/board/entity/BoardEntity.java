package com.zerobase.community.board.entity;

import com.zerobase.community.user.entity.BaseEntity;
import com.zerobase.community.user.entity.UserEntity;
import jakarta.persistence.*;
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

    private String subject;
    private String content;
    private boolean isDisplay;

    // 한 개의 UserEntity에 여러 개의 BoardEntity가 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", referencedColumnName = "loginId", nullable = false)
    private UserEntity user;

}
