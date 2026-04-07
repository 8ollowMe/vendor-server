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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품", description = "업체별 상품 관리 관련 외부 API")
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

  @Operation(
      summary = "상품 등록",
      description =
          "새로운 상품을 등록합니다.<br>'MASTER', 'HUB(담당 허브)', 'VENDOR(본인 업체)' 권한을 가진 사용자만 접근 가능합니다.")
  @PostMapping("vendors/{vendorId}/products")
  public ResponseEntity<ApiResponse> createProduct(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @RequestBody ProductCreateRequest request) {

    ProductCreateResponse response =
        createProductService.create(request.toCommand(vendorId, authUser.userId()));
    return ApiResponse.created(response);
  }

  @Operation(
      summary = "상품 정보 수정",
      description =
          "상품의 상세 정보를 수정합니다.<br>'MASTER', 'HUB(담당 허브)', 'VENDOR(본인 업체)' 권한을 가진 사용자만 접근 가능합니다.")
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

  @Operation(
      summary = "상품 상태 수정",
      description =
          "상품의 판매 상태(ON_SALE(판매중), SOLD_OUT(품절), DISCONTINUED(단종), PENDING(준비 중))를 변경합니다."
              + "<br>'MASTER', 'HUB(담당 허브)', 'VENDOR(본인 업체)' 권한을 가진 사용자만 접근 가능합니다.")
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

  @Operation(
      summary = "상품 삭제",
      description = "등록된 상품을 삭제합니다.<br>'MASTER', 'HUB(담당 허브)' 권한을 가진 사용자만 접근 가능합니다.")
  @DeleteMapping("vendors/{vendorId}/products/{productId}")
  public ResponseEntity<ApiResponse> deleteProduct(
      @ModelAttribute UserContext authUser,
      @PathVariable UUID vendorId,
      @PathVariable UUID productId) {

    deleteProductService.delete(vendorId, productId, authUser.userId());
    return ApiResponse.ok();
  }

  @Operation(
      summary = "특정 업체 상품 목록 조회",
      description =
          "특정 업체에 등록된 상품 목록을 조회, 검색 합니다."
              + "<br>로그인한 모든 사용자가 접근 가능합니다."
              + "<br>상품 이름, 허브 식별자, 상품 코드, 상품 상태 별로 검색 조회를 제공합니다.")
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

  @Operation(
      summary = "전체 상품 목록 조회/검색",
      description =
          "전체 상품 목록을 조회하거나 검색 조건에 맞는 상품을 찾습니다."
              + "<br>로그인한 모든 사용자가 접근 가능합니다."
              + "<br>상품 이름, 허브 식별자, 상품 코드, 상품 상태, 업체 식별자 별로 검색 조회를 제공합니다.")
  @GetMapping("/products")
  public ResponseEntity<ApiResponse> getProducts(
      @ModelAttribute UserContext authUser,
      ProductSearchCondition condition,
      PageRequest pageRequest) {

    Page<ProductSummaryResponse> products =
        productQueryService.getProducts(condition, pageRequest.toPageable());
    return ApiResponse.ok(PageResponse.of(products));
  }

  @Operation(
      summary = "상품 단건 상세 조회",
      description = "특정 상품의 상세 정보를 조회합니다.<br>로그인한 모든 사용자가 접근 가능합니다.")
  @GetMapping("/products/{productId}")
  public ResponseEntity<ApiResponse> getProductDetail(
      @ModelAttribute UserContext authUser, @PathVariable UUID productId) {

    ProductDetailResponse response = productQueryService.getProductDetail(productId);
    return ApiResponse.ok(response);
  }
}
