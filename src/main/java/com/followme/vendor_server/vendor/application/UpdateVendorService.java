package com.followMe.vendor_server.vendor.application;

import com.followMe.vendor_server.vendor.application.command.UpdateVendorCommand;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followMe.vendor_server.vendor.domain.service.PermissionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UpdateVendorService {
  private final VendorRepository vendorRepository;
  private final PermissionChecker permissionChecker;
  private final HubExistenceChecker hubExistenceChecker;

  /** TODO: result 값 수정 */
  public Vendor updateVendorInfo(UpdateVendorCommand command) {
    Vendor vendor =
        vendorRepository
            .findById(command.getVendorId())
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));

    vendor.updateInfo(
        command.getHubId(),
        command.getRequestId(),
        command.getName(),
        command.getVendorType(),
        command.getDescription(),
        command.getOwnerId(),
        command.getOwnerName(),
        command.getAddress(),
        command.getLatitude(),
        command.getLongitude(),
        permissionChecker,
        hubExistenceChecker);

    /** TODO: updatedEvent 발행, outbox 를 하나의 트랜잭션으로 관리하는 기능 추가 해야 됨 */
    log.info("Updated vendor with id {}", vendor.getId());

    return vendor;
  }
}
