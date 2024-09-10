package com.keumbang.auth.controller;

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
  public ResponseEntity<GetTokenResponse> signUp(@RequestBody @Valid final SignUpRequest request) {
    GetTokenResponse response = authService.signUp(request);
    URI uri = URI.create("/members/" + response.memberId());
    return ResponseEntity.created(uri).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<GetTokenResponse> login(@RequestBody @Valid final LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/reissue")
  public ResponseEntity<GetTokenResponse> reissue(
      @RequestBody @Valid final ReissueRequest request) {
    return ResponseEntity.ok(jwtTokenService.reissueToken(request));
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
