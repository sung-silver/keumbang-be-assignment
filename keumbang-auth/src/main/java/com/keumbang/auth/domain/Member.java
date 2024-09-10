package com.keumbang.auth.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.keumbang.auth.domain.enums.MemberRole;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_EMAIL",
          columnNames = {"email"})
    })
@SQLDelete(sql = "UPDATE member SET is_deleted = true, deleted_at = now() WHERE member_id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private MemberRole role;

  @NotNull private String username;

  @NotNull private String email;

  @NotNull private String password;

  private String refreshToken;

  private boolean isDeleted = Boolean.FALSE;

  private LocalDateTime deletedAt;

  @Builder
  private Member(String username, String email, String password) {
    this.role = MemberRole.USER;
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
