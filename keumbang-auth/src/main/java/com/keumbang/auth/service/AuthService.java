package com.keumbang.auth.service;

import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.auth.common.jwt.JwtTokenService;
import com.keumbang.auth.controller.dto.request.LoginRequest;
import com.keumbang.auth.controller.dto.request.SignUpRequest;
import com.keumbang.auth.controller.dto.response.GetTokenResponse;
import com.keumbang.auth.domain.Member;
import com.keumbang.auth.exception.CustomException;
import com.keumbang.auth.repository.MemberRepository;
import com.keumbang.auth.service.vo.MemberAuthVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenService jwtTokenService;
  private final PasswordService passwordService;

  @Transactional
  public GetTokenResponse signUp(final SignUpRequest request) {
    boolean isEmailExist = memberRepository.findByEmail(request.email()).isPresent();

    if (isEmailExist) {
      throw new CustomException(DUPLICATED_EMAIL);
    }

    String encodedPassword = passwordService.encodePassword(request.password());
    final Member member =
        Member.builder()
            .username(request.username())
            .email(request.email())
            .password(encodedPassword)
            .build();

    memberRepository.save(member);

    List<String> roles = new ArrayList<>(Arrays.asList(member.getRole().getRole()));
    MemberAuthVO memberAuthVO = MemberAuthVO.of(member.getMemberId(), roles);

    return jwtTokenService.issueToken(memberAuthVO);
  }

  public GetTokenResponse login(final LoginRequest request) {
    Member member = memberRepository.findByEmailOrThrow(request.email());
    boolean isPasswordValid = passwordService.matches(request.password(), member.getPassword());

    if (!isPasswordValid) {
      throw new CustomException(INVALID_PASSWORD);
    }

    List<String> roles = new ArrayList<>(Arrays.asList(member.getRole().getRole()));
    MemberAuthVO memberAuthVO = MemberAuthVO.of(member.getMemberId(), roles);

    return jwtTokenService.issueToken(memberAuthVO);
  }
}
