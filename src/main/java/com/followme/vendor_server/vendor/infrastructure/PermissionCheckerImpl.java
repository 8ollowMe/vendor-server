package com.followMe.vendor_server.vendor.infrastructure;

import com.followMe.vendor_server.vendor.domain.service.PermissionChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** TODO: FeignClient 도입 시 수정 */
@Component
public class PermissionCheckerImpl implements PermissionChecker {
  @Override
  public boolean hasCreatePermission(UUID hubId, UUID requestId) {
    return true;
  }

  @Override
  public boolean hasUpdatePermission(UUID hubId, UUID requestId) {
    return true;
  }
}
