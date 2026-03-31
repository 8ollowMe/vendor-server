package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.pagination.PageRequest;
import com.followMe.common.pagination.PageResponse;
import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductCreateDto.ProductCreateRequest;
import com.followMe.vendor_server.vendor.application.dto.ProductCreateDto.ProductCreateResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductStatusUpdateDto;
import com.followMe.vendor_server.vendor.application.dto.ProductStatusUpdateDto.ProductStatusUpdateRequest;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ProductController {

  private final CreateProductService createProductService;
  private final UpdateProductService updateProductService;
  private final DeleteProductService deleteProductService;

  private final ProductQueryService productQueryService;

  // userId - 생성 요청자
  @PostMapping("vendors/{vendorId}/products")
  public ApiResponse createProduct(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      UUID VendorId,
      @RequestBody ProductCreateRequest request) {

    ProductCreateResponse response =
        createProductService.create(request.toCommand(VendorId, userId));
    return ApiResponse.success(response);
  }

  // userId - 수정 요청자
  @PutMapping("vendors/{vendorId}/products/{productsId}")
  public ApiResponse updateProduct(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      UUID vendorId,
      UUID productsId,
      @RequestParam ProductUpdateRequest request) {

    ProductUpdateResponse response =
        updateProductService.updateProduct(request.toCommand(vendorId, productsId, userId));
    return ApiResponse.success(response);
  }

  // userId - 수정 요청자
  @PatchMapping("vendors/{vendorId}/products/{productsId}/status")
  public ApiResponse updateProductStatus(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      UUID vendorId,
      UUID productsId,
      ProductStatusUpdateRequest request) {

    ProductStatusUpdateDto.ProductStatusUpdateResponse response =
        updateProductService.updateProductStatus(request.toCommand(vendorId, productsId, userId));
    return ApiResponse.success(response);
  }

  // userId - 삭제 요청자
  @DeleteMapping("vendors/{vendorId}/products/{productsId}")
  public ApiResponse deleteProduct(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      UUID vendorId,
      UUID productId) {

    deleteProductService.delete(vendorId, productId, userId);
    return ApiResponse.success();
  }

  @GetMapping("/vendors/{vendorId}/products")
  public ApiResponse getVendorProducts(
      @PathVariable UUID vendorId, ProductSearchCondition condition, PageRequest pageRequest) {

    Page<ProductSummaryResponse> vendorProducts =
        productQueryService.getVendorProducts(vendorId, condition, pageRequest.toPageable());
    PageResponse<ProductSummaryResponse> response = PageResponse.of(vendorProducts);
    return ApiResponse.success(response);
  }

  @GetMapping("/products")
  public ApiResponse getProducts(ProductSearchCondition condition, PageRequest pageRequest) {

    Page<ProductSummaryResponse> products =
        productQueryService.getProducts(condition, pageRequest.toPageable());
    PageResponse<ProductSummaryResponse> response = PageResponse.of(products);
    return ApiResponse.success(response);
  }

  @GetMapping("/products/{productId}")
  public ApiResponse getProductDetail(@PathVariable UUID productId) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return ApiResponse.success(response);
  }
}
