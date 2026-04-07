package com.followMe.vendor_server.vendor.infrastructure.client.hub.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HubDto {
  boolean success;
  HubInfo data;
  String error;

  @Builder
  @Getter
  @AllArgsConstructor
  public static class HubInfo {
    private UUID hubId;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedA;
  }
}
