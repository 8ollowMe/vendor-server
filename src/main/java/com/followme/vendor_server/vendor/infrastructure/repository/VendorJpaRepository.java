package com.followme.vendor_server.vendor.infrastructure.repository;

import com.followme.vendor_server.vendor.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorJpaRepository extends JpaRepository<Vendor, UUID> {}
