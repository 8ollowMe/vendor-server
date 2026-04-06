package com.followMe.vendor_server.vendor.infrastructure.client.user;

import com.followMe.vendor_server.vendor.application.client.UserClientService;
import com.followMe.vendor_server.vendor.infrastructure.client.user.dto.UserDto.UserInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {
  private final UserFeignClient userFeignClient;

  @Override
  public UserInfo getUserInfo(UUID userId) {
    return userFeignClient.getUser(userId).getData();
  }
}
