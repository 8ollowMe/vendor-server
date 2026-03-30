package com.followme.vendor_server.vendor.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.query.Param;

public interface VendorRepository {

  Optional<Vendor> findById(UUID vendorId);

  Optional<Vendor> findByIdWithProducts(@Param("vendorId") UUID vendorId);

  Vendor save(Vendor vendor);
}
