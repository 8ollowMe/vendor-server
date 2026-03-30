package com.followme.vendor_server.vendor.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendorRepository {

  Optional<Vendor> findById(UUID vendorId);

  @Query("SELECT v FROM Vendor v " + "LEFT JOIN FETCH v.products " + "WHERE v.id = :vendorId")
  Optional<Vendor> findByWithProducts(@Param("vendorId") UUID vendorId);

  Vendor save(Vendor vendor);
}
