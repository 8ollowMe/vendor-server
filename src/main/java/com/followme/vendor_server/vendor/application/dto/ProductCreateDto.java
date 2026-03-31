package com.followMe.vendor_server.vendor.application.dto;

import com.followMe.vendor_server.vendor.application.command.CreateProductCommand;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.ProductStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductCreateDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductCreateRequest {
    private UUID vendorId;
    private String code;
    private String name;
    private String description;
    private Integer price;
    private ProductStatus status;

    public CreateProductCommand toCommand(UUID vendorId, UUID requesterId) {
      return CreateProductCommand.builder()
          .vendorId(vendorId)
          .requesterId(requesterId)
          .code(this.code)
          .name(this.name)
          .description(this.description)
          .price(this.price)
          .status(this.status)
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @Builder
  public static class ProductCreateResponse {
    private UUID productId;
    private Instant createdAt;

    public static ProductCreateResponse from(Product product) {
      return ProductCreateResponse.builder()
          .productId(product.toUuid())
          .createdAt(product.getCreatedAt())
          .build();
    }
  }
}
