package com.keumbang.resource.grpc;

import static com.keumbang.resource.exception.exceptionType.AuthExceptionType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.keumbang.resource.exception.CustomException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServerClient {
  @Value("${auth.server.url}")
  private String authServerUrl;

  @Value("${auth.server.port}")
  private int authServerPort;

  public AuthResponse getAuthorization(String accessToken) {
    // GRPC 서버와의 채널 생성
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(authServerUrl, authServerPort).usePlaintext().build();

    // GRPC 스텁 생성
    AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

    // 요청 생성
    AuthRequest request =
        AuthRequest.newBuilder()
            .setAccessToken(accessToken) // accessToken 설정
            .build();

    try {
      // 요청 전송 및 응답 수신
      AuthResponse response = stub.authenticateMember(request);
      channel.shutdown();

      return response;
    } catch (StatusRuntimeException e) {
      throw new CustomException(AUTHORIZATION_FAILED);
    }
  }
}
