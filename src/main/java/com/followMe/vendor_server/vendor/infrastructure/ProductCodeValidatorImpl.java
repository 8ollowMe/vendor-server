package com.followMe.vendor_server.vendor.infrastructure;

import com.followMe.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followMe.vendor_server.vendor.infrastructure.repository.ProductJpaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCodeValidatorImpl implements ProductCodeValidator {
  private final ProductJpaRepository productJpaRepository;

  @Override
  public boolean isCodeDuplicated(UUID vendorId, String code) {
    return productJpaRepository.existsByVendor_Id_IdAndCode(vendorId, code);
  }
}
