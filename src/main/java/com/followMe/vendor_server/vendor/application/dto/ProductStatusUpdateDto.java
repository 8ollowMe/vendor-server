package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.application.command.UpdateProductStatusCommand;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.ProductStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductStatusUpdateDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductStatusUpdateRequest {
    private ProductStatus status;

    public UpdateProductStatusCommand toCommand(UUID vendorId, UUID productId, UUID requesterId) {
      return UpdateProductStatusCommand.builder()
          .vendorId(vendorId)
          .productId(productId)
          .requesterId(requesterId)
          .status(this.status)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class ProductStatusUpdateResponse {
    private UUID productId;
    private Instant updatedAt;

    public static ProductStatusUpdateResponse from(Product product) {
      return ProductStatusUpdateResponse.builder()
          .productId(product.toUuid())
          .updatedAt(product.getUpdatedAt())
          .build();
    }
  }
}
