package com.followMe.vendor_server.vendor.infrastructure.client.user;

import com.followMe.vendor_server.vendor.domain.UserRole;
import com.followMe.vendor_server.vendor.domain.dto.UserInfo;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
  @Override
  public UserFeignClient create(Throwable cause) {
    log.error("User Server Fallback");

    return new UserFeignClient() {
      @Override
      public UserInfo getUserInfo(UUID userId) {
        return UserInfo.builder().userId(userId).name("목유저").userRole(UserRole.MASTER).build();
      }
    };
  }
}

//  @Override
//  public UserFeignClient create(Throwable cause) {
//
//    log.error("User Server Fallback Triggered. Cause: {}", cause.getMessage());
////     user-server 에 유저가 없는 경우
//        if (cause instanceof FeignException.NotFound) {
//          log.error("User not found in User Server: {}", cause.getMessage());
//          throw new UserClientException(VendorErrorCode.USER_NOT_FOUND);
//        }
//
////     그 외의 경우
//        log.info("Fallback Error: {}", cause.getMessage());
//        throw new VendorException(VendorErrorCode.VENDOR_INTERNAL_ERROR);
//  }
