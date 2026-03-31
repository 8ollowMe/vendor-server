package com.followMe.vendor_server.vendor.domain.dto;

import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserInfo {
  private UUID userId;
  private String name;
  private UserRole userRole;
  private UUID hubId;
  private UUID vendorId;
}
