package com.keumbang.resource.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
  public long getMemberId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    long memberId = (long) authentication.getPrincipal();
    return memberId;
  }
}
