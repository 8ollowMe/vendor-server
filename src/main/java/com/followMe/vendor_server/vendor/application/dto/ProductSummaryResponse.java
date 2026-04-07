package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.domain.ProductStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSummaryResponse {
  UUID productId;
  UUID hubId;
  UUID vendorId;
  String name;
  Integer price;
  String code;
  ProductStatus status;
}
