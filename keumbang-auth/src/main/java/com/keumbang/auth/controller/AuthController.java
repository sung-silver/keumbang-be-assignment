package com.keumbang.auth.controller;

import static com.keumbang.auth.exception.exceptionType.AuthSuccessType.*;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keumbang.auth.common.jwt.JwtTokenService;
import com.keumbang.auth.common.response.SuccessResponse;
import com.keumbang.auth.controller.dto.request.LoginRequest;
import com.keumbang.auth.controller.dto.request.ReissueRequest;
import com.keumbang.auth.controller.dto.request.SignUpRequest;
import com.keumbang.auth.controller.dto.response.GetTokenResponse;
import com.keumbang.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
  private final AuthService authService;
  private final JwtTokenService jwtTokenService;

  @PostMapping("/sign-up")
  public ResponseEntity<SuccessResponse<GetTokenResponse>> signUp(
      @RequestBody @Valid final SignUpRequest request) {
    GetTokenResponse response = authService.signUp(request);
    URI uri = URI.create("/members/" + response.memberId());
    return ResponseEntity.created(uri).body(SuccessResponse.of(SUCCESS_SIGNUP, response));
  }

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<GetTokenResponse>> login(
      @RequestBody @Valid final LoginRequest request) {
    return ResponseEntity.ok(SuccessResponse.of(SUCCESS_LOGIN, authService.login(request)));
  }

  @PostMapping("/reissue")
  public ResponseEntity<SuccessResponse<GetTokenResponse>> reissue(
      @RequestBody @Valid final ReissueRequest request) {
    return ResponseEntity.ok(
        SuccessResponse.of(SUCCESS_TOKEN_REFRESH, jwtTokenService.reissueToken(request)));
  }

  @DeleteMapping("/withdraw")
  public ResponseEntity<Void> withdraw() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long memberId = Long.valueOf((String) authentication.getPrincipal());
    log.info(String.valueOf(memberId));
    authService.withdraw(memberId);
    return ResponseEntity.noContent().build();
  }
}
