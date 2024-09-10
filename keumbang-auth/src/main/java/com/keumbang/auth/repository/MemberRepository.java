package com.keumbang.auth.repository;

import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.keumbang.auth.domain.Member;
import com.keumbang.auth.exception.CustomException;

public interface MemberRepository extends JpaRepository<Member, Long> {
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Member m SET m.refreshToken = :refreshToken WHERE m.memberId = :memberId")
  void updateRefreshToken(
      @Param("memberId") final Long memberId, @Param("refreshToken") final String refreshToken);

  Optional<Member> findByEmail(final String email);

  default Member findByEmailOrThrow(final String email) {
    return findByEmail(email).orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
  }

  Optional<Member> findByMemberId(final Long memberId);

  default Member findByMemberIdOrThrow(final Long memberId) {
    return findByMemberId(memberId).orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
  }
}
