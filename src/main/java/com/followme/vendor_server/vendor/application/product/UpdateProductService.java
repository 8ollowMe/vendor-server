package com.followme.vendor_server.vendor.application.product;

import com.followme.vendor_server.vendor.application.command.UpdateProductCommand;
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
public class UpdateProductService {
  private final VendorRepository vendorRepository;
  private final HubExistenceChecker hubExistenceChecker;
  private final ProductPermissionChecker productPermissionChecker;
  private final ProductCodeValidator productCodeValidator;

  public Product updateProduct(UpdateProductCommand command) {
    Vendor vendor =
        vendorRepository
            .findByWithProducts(command.getVendorId())
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));

    Product product =
        vendor.updateProductInfo(
            command.getProductId(),
            command.getHubId(),
            command.getRequesterId(),
            command.getCode(),
            command.getName(),
            command.getPrice(),
            command.getDescription(),
            hubExistenceChecker,
            productPermissionChecker,
            productCodeValidator);

    /** TODO: updatedEvent 발행, outbox 를 하나의 트랜잭션으로 관리하는 기능 추가 해야 됨 */
    log.info("Updated product with id {}", product.getId());

    return product;
  }
}
