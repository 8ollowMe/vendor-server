package com.followMe.vendor_server.vendor.infrastructure.client.hub;

import static com.followMe.vendor_server.vendor.infrastructure.client.hub.HubFeignClient.HUB_PREFIX;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.infrastructure.client.FeignClientConfig;
import com.followMe.vendor_server.vendor.infrastructure.client.hub.dto.HubDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "hub-server",
    path = HUB_PREFIX,
    configuration = FeignClientConfig.class,
    fallbackFactory = HubFeignClientFallbackFactory.class)
public interface HubFeignClient {
  String HUB_PREFIX = "/api/v1/hubs";

  @CircuitBreaker(name = "hub-server", fallbackMethod = "handleCircuitOpen")
  @Retry(name = "hub-server")
  @GetMapping("/{hubId}")
  HubDto getHub(@PathVariable UUID hubId);

  /*
   * 서킷브레이커 OPEN 상태일 때 처리
   *
   * CallNotPermittedException = 서킷 브레이커 OPEN 상태의 예외
   * 요청을 차단하고,
   * '허브 서비스 상태가 불안하여, 요청이 일시 차단 되었습니다.' 안내 메시지 출력
   */
  default HubDto handleCircuitOpen(UUID hubId, CallNotPermittedException e) {
    throw new VendorException(VendorErrorCode.HUB_CLIENT_CIRCUIT_BREAKER);
  }
}
