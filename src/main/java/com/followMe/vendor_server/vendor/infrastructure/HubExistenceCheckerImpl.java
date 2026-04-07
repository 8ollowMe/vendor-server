package com.followMe.vendor_server.vendor.infrastructure;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followMe.vendor_server.vendor.infrastructure.client.hub.HubFeignClient;
import com.followMe.vendor_server.vendor.infrastructure.client.hub.dto.HubDto.HubInfo;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HubExistenceCheckerImpl implements HubExistenceChecker {
  private final HubFeignClient hubFeignClient;

  @Override
  public boolean hasHub(UUID hubId) {
    HubInfo hubInfo = getHubInfo(hubId);

    validUserInfo(hubInfo);

    return true;
  }

  private HubInfo getHubInfo(UUID hubId) {
    return hubFeignClient.getHub(hubId).getData();
  }

  private void validUserInfo(HubInfo hubInfo) {
    if (Objects.isNull(hubInfo)) {
      throw new VendorException(VendorErrorCode.HUB_NOT_FOUND);
    }
  }
}
