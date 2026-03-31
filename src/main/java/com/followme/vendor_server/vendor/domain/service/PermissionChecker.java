package com.followme.vendor_server.vendor.domain.service;

import java.util.UUID;

/**
 * 권한 체크 인터페이스
 *
 * <p>{@link #hasCreatePermission(UUID, UUID)} - 업체 등록 권한 체크
 *
 * @author 정승현
 */
public interface PermissionChecker {
  boolean hasCreatePermission(UUID hubId, UUID requestId);
}
