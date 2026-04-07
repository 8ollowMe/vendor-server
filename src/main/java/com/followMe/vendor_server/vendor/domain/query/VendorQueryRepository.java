package com.followMe.vendor_server.vendor.domain.query;

import com.followMe.vendor_server.vendor.application.dto.VendorDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorSummaryResponse;
import com.followMe.vendor_server.vendor.application.query.VendorSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorQueryRepository {
  Page<VendorSummaryResponse> searchVendors(VendorSearchCondition condition, Pageable pageable);

  Optional<VendorDetailResponse> findVendorDetailById(UUID vendorId);
}
