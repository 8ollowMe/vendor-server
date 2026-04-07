package com.followMe.vendor_server.vendor.infrastructure.event;

import com.followMe.common.event.Events;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.event.ProductEvents;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsImpl implements ProductEvents {

  @Override
  public void productUpdated(Product product) {
    Events.trigger(ProductUpdatedEvent.of(product));
  }
}
