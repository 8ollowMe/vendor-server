package com.followme.vendor_server.vendor.application.command;

import com.followme.vendor_server.vendor.domain.VendorType;
import jakarta.validation.constraints.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateVendorCommand {

  @NotNull(message = "업체 ID는 필수입니다.")
  private UUID vendorId;

  @NotNull(message = "허브 ID는 필수입니다.")
  private UUID hubId;

  @NotNull(message = "요청 ID는 필수입니다.")
  private UUID requestId;

  @NotBlank(message = "업체 이름은 공백일 수 없습니다.")
  @Size(min = 2, max = 255, message = "업체 이름은 2자 이상 255자 이내여야 합니다.")
  private String name;

  @NotNull(message = "업체 타입은 필수입니다.")
  private VendorType vendorType;

  @Size(max = 255, message = "설명은 255 이내여야 합니다.")
  private String description;

  @NotNull(message = "업체 담당자 ID는 필수입니다.")
  private UUID ownerId;

  @NotBlank(message = "업체 담당자 이름은 공백일 수 없습니다.")
  @Size(max = 50, message = "업체 담당자 이름은 50자 이내여야 합니다.")
  private String ownerName;

  @NotBlank(message = "주소는 필수입니다.")
  @Size(max = 255, message = "업체 주소지는 255자 이내여야 합니다.")
  private String address;

  @NotNull(message = "위도는 필수입니다.")
  private Double latitude;

  @NotNull(message = "경도는 필수입니다.")
  private Double longitude;
}
