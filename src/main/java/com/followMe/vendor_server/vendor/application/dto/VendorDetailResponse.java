package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.domain.VendorType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class VendorDetailResponse {
  UUID vendorId;
  String name;
  VendorType type;
  String description;
  UUID hubId;
  String ownerName;
  String address;
  Double latitude;
  Double longitude;
}
