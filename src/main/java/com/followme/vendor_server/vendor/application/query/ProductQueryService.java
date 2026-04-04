package com.followMe.vendor_server.vendor.application.query;

import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductSummaryResponse;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.query.ProductQueryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {
  private final ProductQueryRepository productQueryRepository;

  public ProductDetailResponse getProductDetail(UUID productId) {
    return productQueryRepository
        .findProductDetailById(productId)
        .orElseThrow(() -> new VendorException(VendorErrorCode.PRODUCT_NOT_FOUND));
  }

  public Page<ProductSummaryResponse> getProducts(
      ProductSearchCondition condition, Pageable pageable) {
    return productQueryRepository.searchProducts(condition, pageable);
  }

  public Page<ProductSummaryResponse> getVendorProducts(
      UUID vendorId, ProductSearchCondition condition, Pageable pageable) {
    condition.toVendorSearch(vendorId);
    return productQueryRepository.searchProducts(condition, pageable);
  }
}
