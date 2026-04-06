package com.followMe.vendor_server.vendor.infrastructure.client.user.dto;

import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
  private boolean success;
  private UserInfo data;

  @Builder
  @Getter
  @AllArgsConstructor
  public static class UserInfo {
    private UUID userId;
    private String username;
    private String name;
    private String address;
    private String phone;
    private String slackId;
    private UserRole role;
    private UUID hubId;
    private UUID vendorId;

    public boolean getT() {
      return false;
    }
  }
}
