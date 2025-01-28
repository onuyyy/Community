package com.zerobase.community.user.repository;

import com.zerobase.community.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByEmail(String email);
}
