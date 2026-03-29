package com.followme.vendor_server.vendor.domain;

import java.util.Optional;
import java.util.UUID;

public interface VendorRepository {

  Optional<Vendor> findById(UUID vendorId);

  Vendor save(Vendor vendor);
}
