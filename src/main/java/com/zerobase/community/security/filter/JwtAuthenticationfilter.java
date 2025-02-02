package com.zerobase.community.security.filter;

import com.zerobase.community.security.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * jwt 토큰을 읽어들여
 * 정상 토큰이면 security context에 저장하는 클래스
 */
@RequiredArgsConstructor
@Slf4j
@Component
// filter를 상속받으면 1 요청당 1 필터가 실행된다
public class JwtAuthenticationfilter extends OncePerRequestFilter {

    // http 프로토콜에서 사용하는 헤더가 Authorization 이다 
    public static final String TOKEN_HEADER = "Authorization";
    // 인증 타입 : Authorization jwt 토큰을 사용하는 경우에는 토큰 앞에 Bearer가 붙는다
    public static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    // Request에 있는 token 꺼내오기
    private String resolveTokenFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        // key에 해당하는 value가 나온다
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 컨트롤러로 가기 전 요청이 들어올 때마다 토큰이 유효한지 아닌지 먼저 확인한다
        String token = resolveTokenFromRequest(request);

        // 토큰 유효성 검사 후 인증 처리
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
            // 인증 객체 생성
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("auth.getAuthorities" + auth.getAuthorities().toString());

            log.info(String.format("[%s] -> %s"
                    , this.tokenProvider.getLoginId(token)
                    , request.getRequestURI()));
        }

        // filter가 연속적으로 실행될 수 있도록 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
