package com.followMe.vendor_server.vendor.application.product;

import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.ProductPermissionChecker;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProductService {
  private final VendorRepository vendorRepository;
  private final ProductPermissionChecker productPermissionChecker;

  public void delete(UUID vendorId, UUID productId, UUID requesterId) {
    Vendor vendor =
        vendorRepository
            .findByIdWithProducts(vendorId)
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));
    vendor.deleteProduct(productId, requesterId, productPermissionChecker);

    log.info("Product Deleted Successfully");
  }
}
