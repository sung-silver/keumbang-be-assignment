package com.keumbang.auth.service.vo;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record MemberAuthVO(@NotNull Long memberId, List<String> roles) {
  public static MemberAuthVO of(Long memberId, List<String> roles) {
    return new MemberAuthVO(memberId, roles);
  }
}
