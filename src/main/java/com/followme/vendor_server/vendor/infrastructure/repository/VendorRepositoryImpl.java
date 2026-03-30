package com.followMe.vendor_server.vendor.infrastructure.repository;

import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VendorRepositoryImpl implements VendorRepository {
  private final VendorJpaRepository vendorJpaRepository;

  @Override
  public Optional<Vendor> findById(UUID vendorId) {
    return vendorJpaRepository.findById(vendorId);
  }

  @Override
  public Optional<Vendor> findByIdWithProducts(UUID vendorId) {
    return vendorJpaRepository.findByIdWithProducts(vendorId);
  }

  @Override
  public Vendor save(Vendor vendor) {
    return vendorJpaRepository.save(vendor);
  }

  @Override
  public Collection<Vendor> findAllByIdWithProducts(Collection<UUID> vendorIds) {
    return vendorJpaRepository.findAllByIdWithProducts(vendorIds);
  }
}
