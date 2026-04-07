package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.application.command.UpdateProductCommand;
import com.followMe.vendor_server.vendor.domain.Product;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductUpdateDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductUpdateRequest {
    private String code;
    private String name;
    private Integer price;
    private String description;

    public UpdateProductCommand toCommand(UUID vendorId, UUID productId, UUID requesterId) {
      return UpdateProductCommand.builder()
          .vendorId(vendorId)
          .productId(productId)
          .requesterId(requesterId)
          .code(this.code)
          .name(this.name)
          .price(this.price)
          .description(this.description)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class ProductUpdateResponse {
    private UUID productId;
    private Instant updatedAt;

    public static ProductUpdateResponse from(Product product) {
      return ProductUpdateResponse.builder()
          .productId(product.toUuid())
          .updatedAt(product.getUpdatedAt())
          .build();
    }
  }
}
