package com.followMe.vendor_server.vendor.application.command;

import com.followMe.vendor_server.vendor.domain.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BulkUpdateProductStatusCommand {

  @NotNull(message = "요청자 ID는 필수입니다.")
  private UUID requesterId;

  @NotEmpty(message = "수정할 상품 목록이 비어있을 수 없습니다.")
  @Valid
  private List<ProductStatusUpdateItem> items;

  @Getter
  @AllArgsConstructor
  public static class ProductStatusUpdateItem {
    @NotNull(message = "업체 ID는 필수입니다.")
    private UUID vendorId;

    @NotNull(message = "상품 ID는 필수입니다.")
    private UUID productId;

    @NotNull(message = "변경할 상태는 필수입니다.")
    private ProductStatus status;
  }
}
