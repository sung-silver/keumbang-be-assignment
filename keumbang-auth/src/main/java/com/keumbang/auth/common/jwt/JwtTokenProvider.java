package com.keumbang.auth.common.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String MEMBER_ID_CLAIM = "memberId";

  private final Key key;
  private final Long accessTokenExpirationPeriod;
  private final Long refreshTokenExpirationPeriod;

  public JwtTokenProvider(@Value("${jwt.secretKey}") final String secretKey,
                          @Value("${jwt.access.expiration}") final Long accessTokenExpirationPeriod,
                          @Value("${jwt.refresh.expiration}") final Long refreshTokenExpirationPeriod) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpirationPeriod = accessTokenExpirationPeriod;
    this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod;
  }

  public String createAccessToken(final Long memberId) {
    Date now = new Date();

    return Jwts.builder()
        .setSubject(ACCESS_TOKEN_SUBJECT)
        .claim(MEMBER_ID_CLAIM, memberId)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
}
