package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.application.command.CreateVendorCommand;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VendorCreateDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class VendorCreateRequest {
    private UUID hubId;
    private String name;
    private VendorType type;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;

    public CreateVendorCommand toCommand(UUID ownerId) {
      return CreateVendorCommand.builder()
          .hubId(this.hubId)
          .ownerId(ownerId)
          .name(this.name)
          .type(this.type)
          .description(this.description)
          .address(this.address)
          .latitude(this.latitude)
          .longitude(this.longitude)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class VendorCreateResponse {
    private UUID vendorId;
    private Instant createdAt;

    public static VendorCreateResponse from(Vendor vendor) {
      return VendorCreateResponse.builder()
          .vendorId(vendor.toUuid())
          .createdAt(vendor.getCreatedAt())
          .build();
    }
  }
}
