package com.followMe.vendor_server.vendor.application.product;

import com.followMe.vendor_server.vendor.application.command.CreateProductCommand;
import com.followMe.vendor_server.vendor.application.dto.ProductCreateDto.ProductCreateResponse;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followMe.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followMe.vendor_server.vendor.domain.service.ProductPermissionChecker;
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

  public ProductCreateResponse create(CreateProductCommand command) {
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

    log.info("Created product");
    return ProductCreateResponse.from(product);
  }
}
