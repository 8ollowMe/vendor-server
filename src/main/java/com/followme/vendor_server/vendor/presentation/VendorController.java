package com.followMe.vendor_server.vendor.presentation;

import com.followMe.common.pagination.PageRequest;
import com.followMe.common.pagination.PageResponse;
import com.followMe.common.response.ApiResponse;
import com.followMe.vendor_server.vendor.application.CreateVendorService;
import com.followMe.vendor_server.vendor.application.DeleteVendorService;
import com.followMe.vendor_server.vendor.application.UpdateVendorService;
import com.followMe.vendor_server.vendor.application.dto.*;
import com.followMe.vendor_server.vendor.application.dto.VendorCreateDto.VendorCreateRequest;
import com.followMe.vendor_server.vendor.application.dto.VendorCreateDto.VendorCreateResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorUpdateDto.VendorUpdateRequest;
import com.followMe.vendor_server.vendor.application.dto.VendorUpdateDto.VendorUpdateResponse;
import com.followMe.vendor_server.vendor.application.query.VendorQueryService;
import com.followMe.vendor_server.vendor.application.query.VendorSearchCondition;
import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class VendorController {

  private final CreateVendorService createVendorService;
  private final UpdateVendorService updateVendorService;
  private final DeleteVendorService deleteVendorService;

  private final VendorQueryService vendorQueryService;

  @PostMapping("/vendors")
  public ApiResponse createVendor(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID hubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID vendorId,
      @RequestBody VendorCreateRequest request) {

    VendorCreateResponse response = createVendorService.create(request.toCommand(userId));
    return ApiResponse.success(response);
  }

  // userId - 수정 요청자
  @PutMapping("/vendors/{vendorId}")
  public ApiResponse updateVendor(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      @RequestBody VendorUpdateRequest request,
      @PathVariable UUID vendorId) {

    VendorUpdateResponse response =
        updateVendorService.updateInfo(request.toCommand(vendorId, userId));
    return ApiResponse.success(response);
  }

  // userId - 삭제 요청자
  @DeleteMapping("/vendors/{vendorId}")
  public ApiResponse deleteVendor(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestHeader("X-User-Role") UserRole userRole,
      @RequestHeader(value = "X-Hub-Id", required = false) UUID xHubId,
      @RequestHeader(value = "X-Vendor-Id", required = false) UUID xVendorId,
      @PathVariable UUID vendorId) {

    deleteVendorService.delete(vendorId, userId);
    return ApiResponse.success();
  }

  @GetMapping("/vendors/{vendorId}")
  public ApiResponse getVendorDetail(@PathVariable UUID vendorId) {
    VendorDetailResponse response = vendorQueryService.getVendorDetail(vendorId);
    return ApiResponse.success(response);
  }

  @GetMapping("/vendors")
  public ApiResponse getVendors(VendorSearchCondition searchCondition, PageRequest pageRequest) {
    Page<VendorSummaryResponse> vendors =
        vendorQueryService.getVendors(searchCondition, pageRequest.toPageable());
    PageResponse<VendorSummaryResponse> response = PageResponse.of(vendors);
    return ApiResponse.success(response);
  }
}
