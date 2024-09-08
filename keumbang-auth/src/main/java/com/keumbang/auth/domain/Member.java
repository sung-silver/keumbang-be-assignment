package com.keumbang.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import com.keumbang.auth.domain.enums.MemberRole;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private MemberRole role;

  @NotNull private String username;

  @NotNull private String email;

  @NotNull private String password;

  private String refreshToken;

  @Builder
  private Member(String username, String email, String password) {
    this.role = MemberRole.USER;
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
