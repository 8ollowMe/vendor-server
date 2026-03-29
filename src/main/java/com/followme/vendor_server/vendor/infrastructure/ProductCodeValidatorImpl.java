package com.followme.vendor_server.vendor.infrastructure;

import com.followme.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followme.vendor_server.vendor.infrastructure.repository.ProductJpaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCodeValidatorImpl implements ProductCodeValidator {
  private final ProductJpaRepository productJpaRepository;

  @Override
  public boolean isCodeDuplicated(UUID vendorId, String code, UUID excludeProductId) {

    if (excludeProductId == null) {
      return productJpaRepository.existsByVendor_Id_IdAndCode(vendorId, code);
    }
    return productJpaRepository.existsByVendor_Id_IdAndCodeAndId_IdNot(
        vendorId, code, excludeProductId);
  }
}
