package com.followme.vendor_server.vendor.infrastructure.repository;

import com.followme.vendor_server.vendor.domain.Vendor;
import com.followme.vendor_server.vendor.domain.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class VendorRepositoryImpl implements VendorRepository {
  private final VendorJpaRepository vendorJpaRepository;

  @Override
  public Optional<Vendor> findById(UUID vendorId) {
    return vendorJpaRepository.findById(vendorId);
  }

  @Override
  public Vendor save(Vendor vendor) {
    return vendorJpaRepository.save(vendor);
  }
}
