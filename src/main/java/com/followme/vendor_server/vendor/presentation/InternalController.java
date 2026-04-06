package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.UserContext;
import com.followMe.vendor_server.vendor.application.command.BulkUpdateProductStatusCommand;
import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorDetailResponse;
import com.followMe.vendor_server.vendor.application.product.UpdateProductService;
import com.followMe.vendor_server.vendor.application.query.ProductQueryService;
import com.followMe.vendor_server.vendor.application.query.VendorQueryService;
import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1")
public class InternalController {
  @ModelAttribute
  public UserContext userContext(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-Role") UserRole role,
      @RequestHeader(value = "X-User-Name", required = false) String userName,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID hubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID vendorId) {
    return new UserContext(userId, role, userName, hubId, vendorId);
  }

  private final VendorQueryService vendorQueryService;
  private final ProductQueryService productQueryService;

  private final UpdateProductService updateProductService;

  @GetMapping("/vendors/{vendorId}")
  public VendorDetailResponse getVendorDetail(
      @PathVariable UUID vendorId, @ModelAttribute UserContext authUser) {

    VendorDetailResponse response = vendorQueryService.getVendorDetail(vendorId);
    return response;
  }

  @GetMapping("/products/{productId}")
  public ProductDetailResponse getProductDetail(
      @PathVariable UUID productId, @ModelAttribute UserContext authUser) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return response;
  }

  @PatchMapping("/products/status")
  public ApiResponse bulkUpdateProductStatus(
      @RequestBody BulkUpdateProductStatusCommand command, @ModelAttribute UserContext authUser) {

    updateProductService.bulkUpdateProductStatus(command);
    return ApiResponse.success();
  }
}
