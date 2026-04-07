package com.followMe.vendor_server.vendor.application.query;

import com.followMe.vendor_server.vendor.domain.VendorType;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class VendorSearchCondition {
  @Size(max = 255, message = "업체명 검색어는 50자 이내여야 합니다.")
  String name;

  VendorType type;

  UUID hubId;
}
