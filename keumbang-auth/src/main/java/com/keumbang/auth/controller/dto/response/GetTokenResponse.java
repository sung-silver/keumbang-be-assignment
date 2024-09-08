package com.keumbang.auth.controller.dto.response;

public record GetTokenResponse(Long memberId, String accessToken, String refreshToken) {
  public static GetTokenResponse of(
      final Long memberId, final String accessToken, final String refreshToken) {
    return new GetTokenResponse(memberId, accessToken, refreshToken);
  }
}
