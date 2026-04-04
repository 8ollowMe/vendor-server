package com.followMe.vendor_server.vendor.infrastructure.client.user;

import com.followMe.vendor_server.vendor.domain.dto.UserInfo;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "user-server",
    path = "/internal/v1/users",
    fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {

  @GetMapping("/{userId}")
  UserInfo getUserInfo(UUID userId);
}
