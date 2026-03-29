package com.followme.vendor_server.vendor.infrastructure.repository;

import com.followme.vendor_server.vendor.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorJpaRepository extends JpaRepository<Vendor, UUID> {}
