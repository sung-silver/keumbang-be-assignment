package com.keumbang.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.auth.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Member m SET m.refreshToken = :refreshToken WHERE m.memberId = :memberId")
  void updateRefreshToken(
      @Param("memberId") final Long memberId, @Param("refreshToken") final String refreshToken);
}
