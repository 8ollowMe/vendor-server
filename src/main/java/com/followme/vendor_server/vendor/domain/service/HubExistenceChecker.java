package com.followme.vendor_server.vendor.domain.service;

import java.util.UUID;

/**
 * 허브 존재 유무를 파악하는 인터페이스
 *
 * @author 정승현
 */
public interface HubExistenceChecker {
  boolean hasHub(UUID hubId);
}
