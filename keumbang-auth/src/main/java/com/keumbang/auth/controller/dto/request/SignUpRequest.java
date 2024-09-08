package com.keumbang.auth.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 한글 또는 영문만 가능합니다.")
        @NotNull(message = "이름은 필수 입력값입니다.")
        String username,
    @Email(message = "올바른 이메일 형식을 사용해야 합니다.") @NotNull String email,
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",
            message =
                "비밀번호는 8자 이상 30자 이하로, 반드시 하나 이상의 소문자, 대문자, 숫자, 그리고 특수문자(@$!%*?&)를 포함해야 하며, 허용된 문자 외에 다른 문자는 사용할 수 없습니다.")
        @NotNull
        String password) {}
