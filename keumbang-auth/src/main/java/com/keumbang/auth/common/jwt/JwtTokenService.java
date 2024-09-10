package com.keumbang.auth.common.jwt;

import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.auth.controller.dto.request.ReissueRequest;
import com.keumbang.auth.controller.dto.response.GetTokenResponse;
import com.keumbang.auth.domain.Member;
import com.keumbang.auth.exception.CustomException;
import com.keumbang.auth.repository.MemberRepository;
import com.keumbang.auth.service.vo.MemberAuthVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtTokenService {
  private static final String MEMBER_ID_CLAIM = "memberId";
  private static final String ROLE_CLAIM = "roles";
  public static String AUTHORIZATION_FORMAT;
  public static String ACCESS_TOKEN_HEADER;

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  public JwtTokenService(
      @Value("${jwt.header.format}") String authorizationFormat,
      @Value("${jwt.access.header}") String accessTokenHeader,
      JwtTokenProvider jwtTokenProvider,
      MemberRepository memberRepository) {
    this.AUTHORIZATION_FORMAT = authorizationFormat;
    this.ACCESS_TOKEN_HEADER = accessTokenHeader;
    this.jwtTokenProvider = jwtTokenProvider;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public GetTokenResponse issueToken(final MemberAuthVO memberAuthVO) {
    Long memberId = memberAuthVO.memberId();
    List<String> roles = memberAuthVO.roles();
    String accessToken = jwtTokenProvider.createAccessToken(memberId, roles);
    String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
    updateRefreshToken(memberId, refreshToken);
    return GetTokenResponse.of(memberId, accessToken, refreshToken);
  }

  @Transactional
  public GetTokenResponse reissueToken(final ReissueRequest request) {
    try {
      String refreshToken = extractToken(request.refreshToken());
      String extractedMemberId = extractMemberIdFromRefreshToken(refreshToken);
      Long memberId = Long.valueOf(extractedMemberId);
      Member member = memberRepository.findByMemberIdOrThrow(memberId);

      validateRefreshToken(refreshToken, member.getRefreshToken());

      List<String> roles = new ArrayList<>(Arrays.asList(member.getRole().getRole()));
      String newAccessToken = jwtTokenProvider.createAccessToken(memberId, roles);
      String newRefreshToken = jwtTokenProvider.createRefreshToken(memberId);

      updateRefreshToken(memberId, newRefreshToken);

      return GetTokenResponse.of(memberId, newAccessToken, newRefreshToken);
    } catch (MalformedJwtException | SignatureException | CustomException e) {
      throw new CustomException(INVALID_REFRESH_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new CustomException(UNAUTHORIZED_REFRESH_TOKEN);
    }
  }

  private String extractToken(final String requestToken) {
    return requestToken.substring(AUTHORIZATION_FORMAT.length()).trim();
  }

  private void updateRefreshToken(Long memberId, String refreshToken) {
    memberRepository.updateRefreshToken(memberId, refreshToken);
  }

  public boolean validateRefreshToken(final String token, final String savedToken) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(token);
    boolean isValidRefreshToken = token.equals(savedToken);
    if (!isValidRefreshToken) {
      throw new CustomException(INVALID_REFRESH_TOKEN);
    }
    return !tokenClaims.getExpiration().before(new Date());
  }

  public boolean validateAccessToken(final String atk) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(atk);
    return !tokenClaims.getExpiration().before(new Date());
  }

  public String extractMemberIdFromRefreshToken(final String rtk) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(rtk);
    return tokenClaims.get(MEMBER_ID_CLAIM).toString();
  }

  public String extractMemberIdFromAccessToken(final String atk) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(atk);
    return tokenClaims.get(MEMBER_ID_CLAIM).toString();
  }

  public Collection<? extends GrantedAuthority> getAuthoritiesFromAccessToken(String accessToken) {
    Claims claims = jwtTokenProvider.getTokenClaims(accessToken);
    List<String> roles = claims.get(ROLE_CLAIM, List.class);

    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  public String getAuthorizationAccessToken(HttpServletRequest request) {
    return request.getHeader(ACCESS_TOKEN_HEADER).substring(7);
  }
}
