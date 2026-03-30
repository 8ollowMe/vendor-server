package com.followMe.vendor_server.vendor.infrastructure.repository;

import com.followMe.vendor_server.vendor.domain.Vendor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendorJpaRepository extends JpaRepository<Vendor, UUID> {

  @Query("SELECT v FROM Vendor v LEFT JOIN FETCH v.products WHERE v.id.id = :vendorId")
  Optional<Vendor> findByIdWithProducts(@Param("vendorId") UUID vendorId);

  @Query("SELECT v FROM Vendor v LEFT JOIN FETCH v.products WHERE v.id.id IN :vendorIds")
  List<Vendor> findAllByIdWithProducts(@Param("vendorIds") Collection<UUID> vendorIds);
}
