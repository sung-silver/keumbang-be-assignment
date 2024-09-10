package com.keumbang.auth.common.security.filter;

import static com.keumbang.auth.common.config.SecurityConfig.*;
import static com.keumbang.auth.common.jwt.JwtTokenService.*;
import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.keumbang.auth.common.jwt.JwtTokenService;
import com.keumbang.auth.common.security.MemberAuthentication;
import com.keumbang.auth.exception.CustomException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (Arrays.stream(PERMIT_PATH).anyMatch(whiteUrl -> request.getRequestURI().equals(whiteUrl))) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!containsAccessToken(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = jwtTokenService.getAuthorizationAccessToken(request);

    try {
      jwtTokenService.validateAccessToken(accessToken);

      String memberId = jwtTokenService.extractMemberIdFromAccessToken(accessToken);
      Collection<? extends GrantedAuthority> authorities =
          jwtTokenService.getAuthoritiesFromAccessToken(accessToken);
      MemberAuthentication memberAuthentication =
          new MemberAuthentication(memberId, null, authorities);

      log.info("Authentication Principal : {}", memberAuthentication.getPrincipal());

      SecurityContextHolder.getContext().setAuthentication(memberAuthentication);

    } catch (MalformedJwtException | SignatureException e) {
      throw new CustomException(INVALID_ACCESS_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new CustomException(UNAUTHORIZED_ACCESS_TOKEN);
    }

    filterChain.doFilter(request, response);
  }

  private boolean containsAccessToken(HttpServletRequest request) {
    String authorization = request.getHeader(ACCESS_TOKEN_HEADER);
    return authorization != null && authorization.startsWith(AUTHORIZATION_FORMAT);
  }
}
