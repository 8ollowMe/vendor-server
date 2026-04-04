package com.followMe.vendor_server.vendor.domain.event;

import com.followMe.vendor_server.vendor.domain.Product;

public interface ProductEvents {
  void productUpdated(Product product);
}
