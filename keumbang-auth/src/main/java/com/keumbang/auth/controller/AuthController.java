package com.keumbang.auth.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keumbang.auth.controller.dto.request.SignUpRequest;
import com.keumbang.auth.controller.dto.response.GetTokenResponse;
import com.keumbang.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<GetTokenResponse> signUp(@RequestBody @Valid final SignUpRequest request) {
    GetTokenResponse response = authService.signUp(request);
    URI uri = URI.create("/members/" + response.memberId());
    return ResponseEntity.created(uri).body(response);
  }
}
