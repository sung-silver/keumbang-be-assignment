package com.keumbang.auth.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull(message = "로그인을 위해 이메일을 입력해주세요") String email,
    @NotNull(message = "로그인을 위해 비밀번호를 입력해주세요") String password) {}
