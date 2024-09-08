package com.keumbang.auth.controller.dto.response;

public record GetTokenResponse(String accessToken, String refreshToken) {
  public static GetTokenResponse of(final String accessToken, final String refreshToken) {
    return new GetTokenResponse(accessToken, refreshToken);
  }
}
