package com.followMe.vendor_server.vendor.infrastructure.client.user;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
  @Override
  public UserFeignClient create(Throwable cause) {
    log.error("User Server Fallback");
    return userId -> {
      if (cause instanceof FeignException.NotFound) {
        throw new VendorException(VendorErrorCode.USER_NOT_FOUND);
      }
      if (cause instanceof FeignException.BadRequest) {
        throw new VendorException(VendorErrorCode.USER_INVALID_INFO);
      }
      throw new UserClientUnavailableException();
    };
  }
}
