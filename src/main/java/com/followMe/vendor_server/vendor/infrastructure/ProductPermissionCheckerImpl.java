package com.followMe.vendor_server.vendor.infrastructure;

import com.followMe.vendor_server.vendor.domain.UserRole;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.ProductPermissionChecker;
import com.followMe.vendor_server.vendor.infrastructure.client.user.UserFeignClient;
import com.followMe.vendor_server.vendor.infrastructure.client.user.dto.UserDto.UserInfo;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductPermissionCheckerImpl implements ProductPermissionChecker {
  private final UserFeignClient userFeignClient;

  @Override
  public boolean hasCreatePermission(
      UUID hubId, UUID ownerId, UUID requesterId, Set<UserRole> permissionRole) {
    UserInfo user = getUserInfo(requesterId);

    validUserInfo(user);

    if (user.getRole().equals(UserRole.HUB)) {
      return isMatchedHubManager(user, hubId);
    }

    if (user.getRole().equals(UserRole.VENDOR)) {
      return isMatchedVendorManager(ownerId, requesterId);
    }

    return permissionRole.contains(user.getRole());
  }

  @Override
  public boolean hasUpdatePermission(
      UUID hubId, UUID ownerId, UUID requesterId, Set<UserRole> permissionRole) {
    UserInfo user = getUserInfo(requesterId);

    validUserInfo(user);

    if (user.getRole().equals(UserRole.HUB)) {
      return isMatchedHubManager(user, hubId);
    }

    if (user.getRole().equals(UserRole.VENDOR)) {
      return isMatchedVendorManager(ownerId, requesterId);
    }

    return permissionRole.contains(user.getRole());
  }

  @Override
  public boolean hasDeletePermission(UUID hubId, UUID requesterId, Set<UserRole> permissionRole) {
    UserInfo user = getUserInfo(requesterId);

    validUserInfo(user);

    if (user.getRole().equals(UserRole.HUB)) {
      return isMatchedHubManager(user, hubId);
    }

    return permissionRole.contains(user.getRole());
  }

  private UserInfo getUserInfo(UUID userId) {
    return userFeignClient.getUser(userId).getData();
  }

  private boolean isMatchedHubManager(UserInfo user, UUID hubId) {
    return user.getHubId().equals(hubId);
  }

  private boolean isMatchedVendorManager(UUID ownerId, UUID requesterId) {
    return ownerId.equals(requesterId);
  }

  private void validUserInfo(UserInfo user) {
    if (Objects.isNull(user)) {
      throw new VendorException(VendorErrorCode.USER_NOT_FOUND);
    }
  }
}
