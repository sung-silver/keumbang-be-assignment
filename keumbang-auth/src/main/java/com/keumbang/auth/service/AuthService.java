package com.keumbang.auth.service;

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
import com.keumbang.auth.repository.MemberRepository;
import com.keumbang.auth.service.vo.MemberAuthVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenService jwtTokenService;

  @Transactional
  public GetTokenResponse signUp(final SignUpRequest request) {
    final Member member =
        Member.builder()
            .username(request.username())
            .email(request.email())
            .password(request.password())
            .build();

    memberRepository.save(member);

    List<String> roles = new ArrayList<>(Arrays.asList(member.getRole().getRole()));
    MemberAuthVO memberAuthVO = MemberAuthVO.of(member.getMemberId(), roles);

    return jwtTokenService.issueToken(memberAuthVO);
  }

  public GetTokenResponse login(final LoginRequest request) {
    Member member =
        memberRepository.findByEmailAndPasswordOrThrow(request.email(), request.password());
    List<String> roles = new ArrayList<>(Arrays.asList(member.getRole().getRole()));
    MemberAuthVO memberAuthVO = MemberAuthVO.of(member.getMemberId(), roles);

    return jwtTokenService.issueToken(memberAuthVO);
  }
}
