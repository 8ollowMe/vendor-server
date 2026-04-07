package com.followMe.vendor_server.vendor.application;

import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.PermissionChecker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class DeleteVendorService {
  private final VendorRepository vendorRepository;
  private final PermissionChecker permissionChecker;

  public void delete(UUID vendorId, UUID requestId) {
    Vendor vendor =
        vendorRepository
            .findByIdWithProducts(vendorId)
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));
    vendor.delete(requestId, permissionChecker);

    log.info("Delete Vendor Deleted Successfully");
  }
}
