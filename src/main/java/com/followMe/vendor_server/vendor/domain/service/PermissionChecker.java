package com.followMe.vendor_server.vendor.domain.service;

import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.Set;
import java.util.UUID;

/**
 * 권한 체크 인터페이스
 *
 * <p>{@link #hasCreatePermission(UUID, UUID)} - 업체 등록 권한 체크
 *
 * @author 정승현
 */
public interface PermissionChecker {
  boolean hasCreatePermission(UUID hubId, UUID requestId, Set<UserRole> permissionRole);

  boolean hasUpdatePermission(
      UUID hubId, UUID requestId, UUID vendorId, Set<UserRole> permissionRole);

  boolean hasDeletePermission(UUID hubId, UUID requestId, Set<UserRole> permissionRole);
}
