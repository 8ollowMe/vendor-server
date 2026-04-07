package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.pagination.PageRequest;
import com.followMe.common.pagination.PageResponse;
import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.CreateVendorService;
import com.followMe.vendor_server.vendor.application.DeleteVendorService;
import com.followMe.vendor_server.vendor.application.UpdateVendorService;
import com.followMe.vendor_server.vendor.application.UserContext;
import com.followMe.vendor_server.vendor.application.dto.*;
import com.followMe.vendor_server.vendor.application.dto.VendorCreateDto.VendorCreateRequest;
import com.followMe.vendor_server.vendor.application.dto.VendorCreateDto.VendorCreateResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorUpdateDto.VendorUpdateRequest;
import com.followMe.vendor_server.vendor.application.dto.VendorUpdateDto.VendorUpdateResponse;
import com.followMe.vendor_server.vendor.application.query.VendorQueryService;
import com.followMe.vendor_server.vendor.application.query.VendorSearchCondition;
import com.followMe.vendor_server.vendor.domain.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "업체", description = "업체/상품 관련 외부 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class VendorController {

  private final CreateVendorService createVendorService;
  private final UpdateVendorService updateVendorService;
  private final DeleteVendorService deleteVendorService;

  private final VendorQueryService vendorQueryService;

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
      summary = "업체 등록",
      description = "업체를 등록합니다. <br>" + "'MASTER', 'HUB(담당 허브)' 권한을 가진 사용자만 접근 가능합니다.")
  @PostMapping("/vendors")
  public ResponseEntity<ApiResponse> createVendor(
      @ModelAttribute UserContext authUser, @RequestBody VendorCreateRequest request) {

    VendorCreateResponse response =
        createVendorService.create(request.toCommand(authUser.userId()));
    return ApiResponse.created(response);
  }

  @Operation(
      summary = "업체 정보 수정",
      description =
          "업체 정보를 수정합니다. <br>" + "'MASTER', 'HUB(담당 허브)', 'VENDOR(본인 업체)' 권한을 가진 사용자만 접근 가능합니다.")
  @PutMapping("/vendors/{vendorId}")
  public ResponseEntity<ApiResponse> updateVendor(
      @ModelAttribute UserContext authUser,
      @RequestBody VendorUpdateRequest request,
      @PathVariable UUID vendorId) {

    VendorUpdateResponse response =
        updateVendorService.updateInfo(request.toCommand(vendorId, authUser.userId()));
    return ApiResponse.ok(response);
  }

  @Operation(
      summary = "업체 삭제",
      description = "업체 삭제<br>" + "'MASTER', 'HUB(담당 허브)' 권한을 가진 사용자만 접근 가능합니다.")
  @DeleteMapping("/vendors/{vendorId}")
  public ResponseEntity<ApiResponse> deleteVendor(
      @ModelAttribute UserContext authUser, @PathVariable UUID vendorId) {

    deleteVendorService.delete(vendorId, authUser.userId());
    return ApiResponse.ok();
  }

  @Operation(summary = "업체 단건 조회", description = "업체 조회<br>" + "로그인 한 모든 사용자가 접근 가능합니다.")
  @GetMapping("/vendors/{vendorId}")
  public ResponseEntity<ApiResponse> getVendorDetail(
      @ModelAttribute UserContext authUser, @PathVariable UUID vendorId) {
    VendorDetailResponse response = vendorQueryService.getVendorDetail(vendorId);
    return ApiResponse.ok(response);
  }

  @Operation(
      summary = "업체 다건 조회/검색",
      description =
          "업체 다건 조회/검색<br>"
              + "로그인 한 모든 사용자가 접근 가능합니다.<br>"
              + "업체 이름, 허브 식졀자, 업체 종류 별로 검색 조회를 제공합니다.")
  @GetMapping("/vendors")
  public ResponseEntity<ApiResponse> getVendors(
      @ModelAttribute UserContext authUser,
      VendorSearchCondition searchCondition,
      PageRequest pageRequest,
      Sort sort) {
    Page<VendorSummaryResponse> vendors =
        vendorQueryService.getVendors(
            searchCondition, pageRequest.toPageable(ifNotSortedReturnCreatedAtDesc(sort)));
    return ApiResponse.ok(PageResponse.of(vendors));
  }

  private Sort ifNotSortedReturnCreatedAtDesc(Sort sort) {
    return sort.isSorted() ? sort : Sort.by(Sort.Direction.DESC, "createdAt");
  }
}
