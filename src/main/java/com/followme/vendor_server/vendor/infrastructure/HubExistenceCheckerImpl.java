package com.followme.vendor_server.vendor.infrastructure;

import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** TODO: FeignClient 도입 시 수정 */
@Component
public class HubExistenceCheckerImpl implements HubExistenceChecker {
  @Override
  public boolean hasHub(UUID hubId) {
    return true;
  }
}
