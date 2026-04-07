package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.application.command.UpdateVendorCommand;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VendorUpdateDto {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class VendorUpdateRequest {
    private String name;
    private VendorType vendorType;
    private String description;
    private UUID ownerId;
    private String ownerName;
    private String address;
    private Double latitude;
    private Double longitude;

    public UpdateVendorCommand toCommand(UUID vendorId, UUID requesterId) {
      return UpdateVendorCommand.builder()
          .vendorId(vendorId)
          .requestId(requesterId)
          .name(this.name)
          .vendorType(this.vendorType)
          .description(this.description)
          .ownerId(this.ownerId)
          .ownerName(this.ownerName)
          .address(this.address)
          .latitude(this.latitude)
          .longitude(this.longitude)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class VendorUpdateResponse {
    private UUID vendorId;
    private Instant updatedAt;

    public static VendorUpdateResponse from(Vendor vendor) {
      return VendorUpdateResponse.builder()
          .vendorId(vendor.toUuid())
          .updatedAt(vendor.getUpdatedAt())
          .build();
    }
  }
}
