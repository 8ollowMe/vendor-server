package com.followme.vendor_server.vendor.infrastructure;

import com.followme.vendor_server.vendor.domain.service.ProductPermissionChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** TODO: FeignClient 도입시 수정 */
@Component
public class ProductPermissionCheckerImpl implements ProductPermissionChecker {

  @Override
  public boolean hasCreatePermission(UUID hubId, UUID requestId, UUID requesterId) {
    return true;
  }

  @Override
  public boolean hasUpdatePermission(UUID hubId, UUID ownerId, UUID requesterId) {
    return true;
  }
}
