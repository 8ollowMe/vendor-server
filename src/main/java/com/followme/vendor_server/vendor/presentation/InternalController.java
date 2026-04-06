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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "내부 API", description = "업체/상품 관련 내부 API")
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

  @Operation(summary = "업체 단건 조회", description = "업체의 상세 정보를 조회합니다. <br>" + "로그인한 사용자만 접근 가능합니다.")
  @GetMapping("/vendors/{vendorId}")
  public VendorDetailResponse getVendorDetail(
      @PathVariable UUID vendorId, @ModelAttribute UserContext authUser) {

    VendorDetailResponse response = vendorQueryService.getVendorDetail(vendorId);
    return response;
  }

  @Operation(summary = "상품 단건 조회", description = "상품의 상세 정보를 조회합니다. <br>" + "로그인한 사용자만 접근 가능합니다.")
  @GetMapping("/products/{productId}")
  public ProductDetailResponse getProductDetail(
      @PathVariable UUID productId, @ModelAttribute UserContext authUser) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return response;
  }

  @Operation(
      summary = "다건 상품 상태 변경",
      description =
          "다건의 상품의 상태를 한 번에 변경 합니다. <br>"
              + "'MASTER', 'HUB(담당 허브)', 'VENDOR(본인 업체)' 권한을 가진 사용자만 접근 가능합니다.")
  @PatchMapping("/products/status")
  public ApiResponse bulkUpdateProductStatus(
      @RequestBody BulkUpdateProductStatusCommand command, @ModelAttribute UserContext authUser) {

    updateProductService.bulkUpdateProductStatus(command);
    return ApiResponse.success();
  }
}
