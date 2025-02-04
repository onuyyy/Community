package com.zerobase.community.user.entity;

import com.zerobase.community.board.entity.BoardEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "User")
public class UserEntity extends BaseEntity implements UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    private String loginId;

    private String password;
    private String email;
    private LocalDate birth;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "login_id"))
    @Column(name = "role")
    private List<String> roles;

    @Column(name = "enabled")
    private boolean enabled;  // 활성화 여부

    // 하나의 userentity가 여러 개의 boardentity 가질 수 있음
    // mappedBy : 관계의 주인을 정의, BoardEntity의 user 필드가 이 관계의 주인
    @OneToMany(mappedBy = "user")
    private List<BoardEntity> user_boards = new ArrayList<>();

    /*
        유저의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
