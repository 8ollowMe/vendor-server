package com.followMe.vendor_server.vendor.application.query;

import com.followMe.vendor_server.vendor.application.dto.VendorDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorSummaryResponse;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.query.VendorQueryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorQueryService {
  private final VendorQueryRepository vendorQueryRepository;

  public VendorDetailResponse getVendorDetail(UUID vendorId) {

    return vendorQueryRepository
        .findVendorDetailById(vendorId)
        .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));
  }

  public Page<VendorSummaryResponse> getVendors(
      VendorSearchCondition condition, Pageable pageable) {
    return vendorQueryRepository.searchVendors(condition, pageable);
  }
}
