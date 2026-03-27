package com.followme.vendor_server.vendor.application;

import com.followme.vendor_server.vendor.application.command.CreateVendorCommand;
import com.followme.vendor_server.vendor.domain.Vendor;
import com.followme.vendor_server.vendor.domain.VendorRepository;
import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followme.vendor_server.vendor.domain.service.PermissionChecker;
import com.followme.vendor_server.vendor.domain.vo.Address;
import com.followme.vendor_server.vendor.domain.vo.Owner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateVendorService {
  private final VendorRepository vendorRepository;
  private final PermissionChecker permissionChecker;
  private final HubExistenceChecker hubExistenceChecker;

  public Vendor save(CreateVendorCommand command) {
    Vendor vendor =
        Vendor.create(
            command.getHubId(),
            Owner.of(command.getOwnerId(), command.getOwnerName()),
            command.getName(),
            command.getType(),
            command.getDescription(),
            Address.of(command.getAddress(), command.getLatitude(), command.getLongitude()),
            permissionChecker,
            hubExistenceChecker);

    Vendor saved = vendorRepository.save(vendor);
    log.debug("Saved vendor {}", saved);
    return saved;
  }
}
