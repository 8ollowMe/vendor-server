package com.followMe.vendor_server.vendor.infrastructure.repository;

import com.followMe.vendor_server.vendor.domain.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {

  boolean existsByVendor_Id_IdAndCode(UUID vendorId, String code);
}
