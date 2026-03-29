package com.followme.vendor_server.vendor.application.command;

import jakarta.validation.constraints.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProductCommand {
  @NotNull(message = "업체 ID는 필수입니다.")
  private UUID vendorId;

  @NotNull(message = "상품 ID는 필수입니다.")
  private UUID productId;

  @NotNull(message = "허브 ID는 필수입니다.")
  private UUID hubId;

  @NotNull(message = "요청자 ID는 필수입니다.")
  private UUID requesterId;

  @NotBlank(message = "상품 코드는 필수입니다.")
  @Size(max = 50, message = "상품 코드는 50자 이내여야 합니다.")
  private String code;

  @NotBlank(message = "상품 이름은 필수입니다.")
  @Size(max = 255, message = "상품 이름은 255자 이내여야 합니다.")
  private String name;

  @NotNull(message = "가격은 필수입니다.")
  @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
  private Integer price;

  @Size(max = 255, message = "상품 설명은 255자 이내여야 합니다.")
  private String description;
}
