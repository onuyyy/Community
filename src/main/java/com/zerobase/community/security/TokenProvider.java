package com.zerobase.community.security;

import com.zerobase.community.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Jwt 사용을 위해
 * 발급하고 검증하는 로직을 구현한 클래스
 */

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hours
    private static final String KEY_ROLES = "roles";
    private final UserRepository userRepository;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성 메소드
     * @param loginId
     * @param roles
     * @return
     */
    public String generateToken(String loginId, List<String> roles) {

        // 사용자 권한 정보 저장, Claims 생성
        Claims claims = Jwts.claims().setSubject(loginId);
        // key - value 형태로 저장해야 한다
        claims.put(KEY_ROLES, roles);

        System.out.println(roles.toString());

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims) // 토큰에 포함될 모든 클레임을 한 번에 설정
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀 키
                .compact();
    }


    public String getLoginId(String token) {
        return this.parseClaims(token).getSubject();
    }

    // 토큰이 유효한지 확인한다
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token))
            return false;

        var claims = this.parseClaims(token);

        return !claims.getExpiration().before(new Date());
    }

    // JWT 토큰에서 클레임 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 사용자 정보 가져오기
    public Authentication getAuthentication(String jwt) {
        String loginId = getLoginId(jwt);

        // UserDetailsService로 사용자 정보 로드
        UserDetails userDetails = userRepository.findById(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginId));

        System.out.println("getAuthentication  : " + userDetails.getAuthorities().toString());

        // 사용자 정보와 사용자의 권한 정보를 가지고 있다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
