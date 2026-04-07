package com.followMe.vendor_server.vendor.application;

import com.followMe.vendor_server.vendor.application.client.UserClientService;
import com.followMe.vendor_server.vendor.application.command.CreateVendorCommand;
import com.followMe.vendor_server.vendor.application.dto.VendorCreateDto.VendorCreateResponse;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
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
public class CreateVendorService {
  private final VendorRepository vendorRepository;
  private final PermissionChecker permissionChecker;
  private final HubExistenceChecker hubExistenceChecker;
  private final UserClientService userClientService;

  public VendorCreateResponse create(CreateVendorCommand command) {
    String ownerName = userClientService.getUserInfo(command.getOwnerId()).getName();

    Vendor vendor =
        Vendor.create(
            command.getHubId(),
            command.getOwnerId(),
            ownerName,
            command.getName(),
            command.getType(),
            command.getDescription(),
            command.getAddress(),
            command.getLatitude(),
            command.getLongitude(),
            permissionChecker,
            hubExistenceChecker);

    Vendor saved = vendorRepository.save(vendor);

    log.info("Created vendor");
    return VendorCreateResponse.from(saved);
  }
}
