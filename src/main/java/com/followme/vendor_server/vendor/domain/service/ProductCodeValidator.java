package com.followMe.vendor_server.vendor.domain.service;

import java.util.UUID;

public interface ProductCodeValidator {
  boolean isCodeDuplicated(UUID vendorId, String productCode);
}
