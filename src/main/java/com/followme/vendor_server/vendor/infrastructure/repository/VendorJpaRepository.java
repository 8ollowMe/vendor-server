package com.followme.vendor_server.vendor.infrastructure.repository;

import com.followme.vendor_server.vendor.domain.Vendor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorJpaRepository extends JpaRepository<Vendor, UUID> {
  @Query("SELECT v FROM Vendor v " + "LEFT JOIN FETCH v.products " + "WHERE v.id = :vendorId")
  Optional<Vendor> findByIdWithProducts(UUID vendorId);
}
