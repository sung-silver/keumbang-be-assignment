package com.keumbang.auth.common.jwt;

import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
  private static final String MEMBER_ID_CLAIM = "memberId";

  @Value("${jwt.header.format}")
  private String AUTHORIZATION_FORMAT;

  @Value("${jwt.access.header}")
  private String ACCESS_TOKEN_HEADER;

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Transactional
  public GetTokenResponse issueToken(final MemberAuthVO memberAuthVO) {
    Long memberId = memberAuthVO.memberId();
    List<String> roles = memberAuthVO.roles();
    String accessToken = jwtTokenProvider.createAccessToken(memberId, roles);
    String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
    updateRefreshToken(memberId, refreshToken);
    return GetTokenResponse.of(memberId, accessToken, refreshToken);
  }

  public GetTokenResponse reissueToken(final ReissueRequest request) {
    try {
      String refreshToken = extractToken(request.refreshToken());
      String extractedMemberId = extractMemberIdFromRefreshToken(refreshToken);
      Long memberId = Long.valueOf(extractedMemberId);
      Member member = memberRepository.findByMemberIdOrThrow(memberId);

      validateToken(refreshToken, member.getRefreshToken());

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

  public boolean validateToken(final String token, final String savedToken) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(token);
    boolean isValidRefreshToken = token.equals(savedToken);
    if (!isValidRefreshToken) {
      throw new CustomException(INVALID_REFRESH_TOKEN);
    }
    return !tokenClaims.getExpiration().before(new Date());
  }

  public String extractMemberIdFromRefreshToken(final String rtk) {
    Claims tokenClaims = jwtTokenProvider.getTokenClaims(rtk);
    return tokenClaims.get(MEMBER_ID_CLAIM).toString();
  }
}
