package com.followMe.vendor_server.vendor.application.command;

import com.followMe.vendor_server.vendor.domain.ProductStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateProductCommand {
  private UUID vendorId;
  private UUID requesterId;
  private String code;
  private String name;
  private String description;
  private Integer price;
  private ProductStatus status;
}
