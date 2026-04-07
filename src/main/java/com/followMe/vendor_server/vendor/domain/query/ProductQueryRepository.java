package com.followMe.vendor_server.vendor.domain.query;

import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductSummaryResponse;
import com.followMe.vendor_server.vendor.application.query.ProductSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {
  Page<ProductSummaryResponse> searchProducts(ProductSearchCondition condition, Pageable pageable);

  Optional<ProductDetailResponse> findProductDetailById(UUID productId);
}
