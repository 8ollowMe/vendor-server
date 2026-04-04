package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.command.BulkUpdateProductStatusCommand;
import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorDetailResponse;
import com.followMe.vendor_server.vendor.application.product.UpdateProductService;
import com.followMe.vendor_server.vendor.application.query.ProductQueryService;
import com.followMe.vendor_server.vendor.application.query.VendorQueryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1")
public class InternalController {
  private final VendorQueryService vendorQueryService;
  private final ProductQueryService productQueryService;

  private final UpdateProductService updateProductService;

  @GetMapping("/vendors/{vendorId}")
  public VendorDetailResponse getVendorDetail(@PathVariable UUID vendorId) {

    VendorDetailResponse response = vendorQueryService.getVendorDetail(vendorId);
    return response;
  }

  @GetMapping("/products/{productId}")
  public ProductDetailResponse getProductDetail(@PathVariable UUID productId) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return response;
  }

  @PatchMapping("/products/status")
  public ApiResponse bulkUpdateProductStatus(@RequestBody BulkUpdateProductStatusCommand command) {

    updateProductService.bulkUpdateProductStatus(command);
    return ApiResponse.success();
  }
}
