package com.followMe.vendor_server.vendor.application.query;

import com.followMe.vendor_server.vendor.domain.ProductStatus;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchCondition {
  @Size(max = 255, message = "상품 이름 검색어는 255자 이내여야 합니다.")
  private String name;

  private UUID hubId;

  @Size(max = 50, message = "상품 코드 검색어는 50자 이내여야 합니다.")
  private String code;

  private ProductStatus status;
  private UUID vendorId;

  public ProductSearchCondition toVendorSearch(UUID vendorId) {
    return ProductSearchCondition.builder()
        .name(this.name)
        .vendorId(vendorId)
        .hubId(this.hubId)
        .code(this.code)
        .status(this.status)
        .hubId(this.hubId)
        .build();
  }

  public ProductSearchCondition toHubSearch(UUID hubId) {
    return ProductSearchCondition.builder()
        .name(this.name)
        .hubId(hubId)
        .code(this.code)
        .status(this.status)
        .vendorId(this.vendorId)
        .build();
  }
}
