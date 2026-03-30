package com.followme.vendor_server.vendor.application.command;

import com.followme.vendor_server.vendor.domain.ProductStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProductStatusCommand {

  @NotNull(message = "업체 ID는 필수입니다.")
  private UUID vendorId;

  @NotNull(message = "상품 ID는 필수입니다.")
  private UUID productId;

  @NotNull(message = "요청자 ID는 필수입니다.")
  private UUID requesterId;

  @NotNull(message = "변경할 상품 상태는 필수입니다.")
  private ProductStatus status;
}
