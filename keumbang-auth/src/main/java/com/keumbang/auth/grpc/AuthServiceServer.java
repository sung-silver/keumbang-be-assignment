package com.keumbang.auth.grpc;

import static com.keumbang.auth.exception.exceptionType.AuthExceptionType.*;

import java.util.List;

import com.keumbang.auth.common.jwt.JwtTokenService;
import com.keumbang.auth.exception.CustomException;

import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class AuthServiceServer extends AuthServiceGrpc.AuthServiceImplBase {
  private final JwtTokenService jwtTokenService;
  private static final Status SUCCESS = Status.SUCCESS;
  private static final Status FAIL = Status.FAILURE;

  @Override
  public void authenticateMember(
      AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
    String accessToken = getAccessToken(request.getAccessToken());
    log.info("[jwtService] authenticateUser - token : {}", accessToken);

    AuthResponse.Builder responseBuilder = AuthResponse.newBuilder();
    responseBuilder.setStatus(FAIL);

    try {
      jwtTokenService.validateAccessToken(accessToken);

      long memberId = Long.parseLong(jwtTokenService.extractMemberIdFromAccessToken(accessToken));
      List<String> roles = jwtTokenService.extractRolesFromAccessToken(accessToken);

      responseBuilder.setStatus(SUCCESS).setId(memberId).addAllRoles(roles);
    } catch (MalformedJwtException | SignatureException | CustomException e) {
      log.error("{}: {}", INVALID_ACCESS_TOKEN.getMessage(), accessToken);
    } catch (ExpiredJwtException e) {
      log.error("{}: {}", UNAUTHORIZED_ACCESS_TOKEN.getMessage(), accessToken);
    }

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  private String getAccessToken(String accessToken) {
    return accessToken.substring(7);
  }
}
