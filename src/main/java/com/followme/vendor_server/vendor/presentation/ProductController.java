package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.pagination.PageRequest;
import com.followMe.common.pagination.PageResponse;
import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.UserContext;
import com.followMe.vendor_server.vendor.application.dto.ProductCreateDto.ProductCreateRequest;
import com.followMe.vendor_server.vendor.application.dto.ProductCreateDto.ProductCreateResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductStatusUpdateDto.ProductStatusUpdateRequest;
import com.followMe.vendor_server.vendor.application.dto.ProductStatusUpdateDto.ProductStatusUpdateResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductSummaryResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductUpdateDto.ProductUpdateRequest;
import com.followMe.vendor_server.vendor.application.dto.ProductUpdateDto.ProductUpdateResponse;
import com.followMe.vendor_server.vendor.application.product.CreateProductService;
import com.followMe.vendor_server.vendor.application.product.DeleteProductService;
import com.followMe.vendor_server.vendor.application.product.UpdateProductService;
import com.followMe.vendor_server.vendor.application.query.ProductQueryService;
import com.followMe.vendor_server.vendor.application.query.ProductSearchCondition;
import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ProductController {

  private final CreateProductService createProductService;
  private final UpdateProductService updateProductService;
  private final DeleteProductService deleteProductService;

  private final ProductQueryService productQueryService;

  @ModelAttribute
  public UserContext userContext(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-Role") UserRole role,
      @RequestHeader(value = "X-User-Name", required = false) String userName,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID hubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID vendorId) {
    return new UserContext(userId, role, userName, hubId, vendorId);
  }

  // userId - 생성 요청자
  @PostMapping("vendors/{vendorId}/products")
  public ResponseEntity<ApiResponse> createProduct(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @RequestBody ProductCreateRequest request) {

    ProductCreateResponse response =
        createProductService.create(request.toCommand(vendorId, authUser.userId()));
    return ApiResponse.created(response);
  }

  // userId - 수정 요청자
  @PutMapping("vendors/{vendorId}/products/{productId}")
  public ResponseEntity<ApiResponse> updateProduct(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @PathVariable UUID productId,
      @RequestBody ProductUpdateRequest request) {

    ProductUpdateResponse response =
        updateProductService.updateProduct(
            request.toCommand(vendorId, productId, authUser.userId()));
    return ApiResponse.ok(response);
  }

  // userId - 수정 요청자
  @PatchMapping("vendors/{vendorId}/products/{productId}/status")
  public ResponseEntity<ApiResponse> updateProductStatus(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @PathVariable UUID productId,
      @RequestBody ProductStatusUpdateRequest request) {

    ProductStatusUpdateResponse response =
        updateProductService.updateProductStatus(
            request.toCommand(vendorId, productId, authUser.userId()));
    return ApiResponse.ok(response);
  }

  // userId - 삭제 요청자
  @DeleteMapping("vendors/{vendorId}/products/{productId}")
  public ResponseEntity<ApiResponse> deleteProduct(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @PathVariable UUID productId) {

    deleteProductService.delete(vendorId, productId, authUser.userId());
    return ApiResponse.ok();
  }

  @GetMapping("/vendors/{vendorId}/products")
  public ResponseEntity<ApiResponse> getVendorProducts(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      ProductSearchCondition condition,
      PageRequest pageRequest) {

    Page<ProductSummaryResponse> vendorProducts =
        productQueryService.getVendorProducts(vendorId, condition, pageRequest.toPageable());
    return ApiResponse.ok(PageResponse.of(vendorProducts));
  }

  @GetMapping("/products")
  public ResponseEntity<ApiResponse> getProducts(
      @ModelAttribute UserContext authUser,
      ProductSearchCondition condition,
      PageRequest pageRequest) {

    Page<ProductSummaryResponse> products =
        productQueryService.getProducts(condition, pageRequest.toPageable());
    return ApiResponse.ok(PageResponse.of(products));
  }

  @GetMapping("/products/{productId}")
  public ResponseEntity<ApiResponse> getProductDetail(
      @ModelAttribute UserContext authUser, @PathVariable UUID productId) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return ApiResponse.ok(response);
  }
}
