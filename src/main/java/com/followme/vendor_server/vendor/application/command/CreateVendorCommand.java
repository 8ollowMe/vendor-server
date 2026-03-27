package com.followme.vendor_server.vendor.application.command;

import com.followme.vendor_server.vendor.domain.VendorType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateVendorCommand {
  private UUID hubId;
  private UUID ownerId;
  private String ownerName;
  private String name;
  private VendorType type;
  private String description;
  private String address;
  private Double latitude;
  private Double longitude;
}
