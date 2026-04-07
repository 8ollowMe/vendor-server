package com.followMe.vendor_server.vendor.domain.vo;

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
public class VendorId {

  @Column(name = "vendor_id", nullable = false)
  private UUID id;

  public static VendorId of(UUID id) {
    return new VendorId(Objects.requireNonNull(id));
  }

  public static VendorId of() {
    return new VendorId(UUID.randomUUID());
  }
}
