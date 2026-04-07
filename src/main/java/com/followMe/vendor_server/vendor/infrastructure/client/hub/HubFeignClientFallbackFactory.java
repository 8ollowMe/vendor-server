package com.followMe.vendor_server.vendor.infrastructure.client.hub;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HubFeignClientFallbackFactory implements FallbackFactory<HubFeignClient> {
  @Override
  public HubFeignClient create(Throwable cause) {
    log.error("Hub Server Fallback");
    return hubId -> {
      if (cause instanceof FeignException.NotFound) {
        throw new VendorException(VendorErrorCode.HUB_NOT_FOUND);
      }
      if (cause instanceof FeignException.BadRequest) {
        throw new VendorException(VendorErrorCode.HUB_INVALID_INFO);
      }
      throw new HubClientUnavailableException();
    };
  }
}
