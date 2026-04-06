package com.followMe.vendor_server.vendor.infrastructure.client.user;

import static com.followMe.vendor_server.vendor.infrastructure.client.user.UserFeignClient.USER_PREFIX;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.infrastructure.client.FeignClientConfig;
import com.followMe.vendor_server.vendor.infrastructure.client.user.dto.UserDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-server",
    path = USER_PREFIX,
    configuration = FeignClientConfig.class,
    fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {
  String USER_PREFIX = "/internal/v1/users";

  @CircuitBreaker(name = "user-server", fallbackMethod = "handleCircuitOpen")
  @Retry(name = "user-server")
  @GetMapping("/{userId}")
  UserDto getUser(@PathVariable UUID userId);

  /*
   * 서킷브레이커 OPEN 상태일 때 처리
   *
   * CallNotPermittedException = 서킷 브레이커 OPEN 상태의 예외
   * 요청을 차단하고,
   * '유저 서비스 상태가 불안하여, 요청이 일시 차단 되었습니다.' 안내 메시지 출력
   */
  default UserDto handleCircuitOpen(UUID userId, CallNotPermittedException e) {
    throw new VendorException(VendorErrorCode.USER_CLIENT_CIRCUIT_BREAKER);
  }
}
