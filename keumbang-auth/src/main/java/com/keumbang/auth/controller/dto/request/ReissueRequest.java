package com.keumbang.auth.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReissueRequest(@NotNull(message = "필수 입력 값 입니다") String refreshToken) {}
