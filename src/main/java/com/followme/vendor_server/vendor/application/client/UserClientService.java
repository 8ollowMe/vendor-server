package com.followMe.vendor_server.vendor.application.client;

import com.followMe.vendor_server.vendor.domain.dto.UserInfo;
import java.util.UUID;

public interface UserClientService {
  UserInfo getUserInfo(UUID userId);
}
