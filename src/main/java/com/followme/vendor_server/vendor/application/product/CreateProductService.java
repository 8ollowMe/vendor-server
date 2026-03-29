package com.followme.vendor_server.vendor.application.product;

import com.followme.vendor_server.vendor.application.command.CreateProductCommand;
import com.followme.vendor_server.vendor.domain.Product;
import com.followme.vendor_server.vendor.domain.Vendor;
import com.followme.vendor_server.vendor.domain.VendorRepository;
import com.followme.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followme.vendor_server.vendor.domain.exception.VendorException;
import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followme.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followme.vendor_server.vendor.domain.service.ProductPermissionChecker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CreateProductService {
  private final VendorRepository vendorRepository;
  private final ProductPermissionChecker productPermissionChecker;
  private final HubExistenceChecker hubExistenceChecker;
  private final ProductCodeValidator productCodeValidator;

  /** TODO: result 값 수정 */
  public Product create(CreateProductCommand command) {
    Vendor vendor =
        vendorRepository
            .findById(command.getVendorId())
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));

    Product product =
        vendor.addProduct(
            command.getRequesterId(),
            command.getCode(),
            command.getName(),
            command.getDescription(),
            command.getPrice(),
            command.getStatus(),
            productPermissionChecker,
            hubExistenceChecker,
            productCodeValidator);

    vendorRepository.save(vendor);
    log.info("Created product with id {}", product.getId());
    return product;
  }
}
