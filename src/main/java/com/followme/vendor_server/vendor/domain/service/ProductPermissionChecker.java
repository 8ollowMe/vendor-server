package com.followme.vendor_server.vendor.domain.service;

import java.util.UUID;

public interface ProductPermissionChecker {
  boolean hasCreatePermission(UUID hubId, UUID ownerId, UUID requesterId);

  boolean hasUpdatePermission(UUID hubId, UUID ownerId, UUID requesterId);
}
