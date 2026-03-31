package com.followme.vendor_server.vendor.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductId {
  @Column(name = "product_id", nullable = false)
  private UUID id;

  public static ProductId of(UUID id) {
    return new ProductId(Objects.requireNonNull(id));
  }

  public static ProductId of() {
    return new ProductId(UUID.randomUUID());
  }
}
