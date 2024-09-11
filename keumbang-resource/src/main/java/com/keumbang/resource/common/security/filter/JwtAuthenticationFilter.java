package com.keumbang.resource.common.security.filter;

import static com.keumbang.resource.common.config.SecurityConfig.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.keumbang.resource.common.security.MemberAuthentication;
import com.keumbang.resource.grpc.AuthResponse;
import com.keumbang.resource.grpc.AuthServerClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final AuthServerClient authServerClient;

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
    String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
    AuthResponse authResponse = authServerClient.getAuthorization(accessToken);
    Long memberId = authResponse.getId();
    List<String> roles = authResponse.getRolesList();
    Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromList(roles);
    MemberAuthentication memberAuthentication =
        new MemberAuthentication(memberId, null, authorities);

    log.info("Authentication Principal : {}", memberAuthentication.getPrincipal());

    SecurityContextHolder.getContext().setAuthentication(memberAuthentication);

    filterChain.doFilter(request, response);
  }

  private Collection<? extends GrantedAuthority> getAuthoritiesFromList(List<String> roles) {
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  private boolean containsAccessToken(HttpServletRequest request) {
    String authorization = request.getHeader(ACCESS_TOKEN_HEADER);
    return authorization != null && authorization.startsWith(AUTHORIZATION_FORMAT);
  }
}
