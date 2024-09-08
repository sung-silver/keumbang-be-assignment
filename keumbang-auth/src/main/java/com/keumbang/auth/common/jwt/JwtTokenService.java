package com.keumbang.auth.common.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.auth.controller.dto.response.GetTokenResponse;
import com.keumbang.auth.repository.MemberRepository;
import com.keumbang.auth.service.vo.MemberAuthVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
  private static final String MEMBER_ID_CLAIM = "memberId";

  @Value("${jwt.header.format}")
  private String AUTHORIZATION_FORMAT;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

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

  private void updateRefreshToken(Long memberId, String refreshToken) {
    memberRepository.updateRefreshToken(memberId, refreshToken);
  }
}
