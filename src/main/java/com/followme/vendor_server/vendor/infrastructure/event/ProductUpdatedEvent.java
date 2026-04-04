package com.followMe.vendor_server.vendor.infrastructure.event;

import com.followMe.common.event.BaseEvent;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.event.DomainTypes;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductUpdatedEvent extends BaseEvent {
  private static final String domain = DomainTypes.PRODUCT.getType();

  public record Payload(UUID productId, String productCode, String productName) {}

  private ProductUpdatedEvent(UUID productId, Object payload) {
    super(domain, productId, payload);
  }

  public static ProductUpdatedEvent of(Product product) {
    return new ProductUpdatedEvent(
        product.toUuid(), new Payload(product.toUuid(), product.getCode(), product.getName()));
  }
}
