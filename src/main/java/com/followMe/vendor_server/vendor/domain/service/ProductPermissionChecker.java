package com.followMe.vendor_server.vendor.domain.service;

import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.Set;
import java.util.UUID;

public interface ProductPermissionChecker {
  boolean hasCreatePermission(
      UUID hubId, UUID ownerId, UUID requesterId, Set<UserRole> permissionRole);

  boolean hasUpdatePermission(
      UUID hubId, UUID ownerId, UUID requesterId, Set<UserRole> permissionRole);

  boolean hasDeletePermission(UUID hubId, UUID requesterId, Set<UserRole> permissionRole);
}
