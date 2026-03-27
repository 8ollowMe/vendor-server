package com.followme.vendor_server.vendor.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.*;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  public static Address of(String address, Double latitude, Double longitude) {
    if (!StringUtils.hasText(address)) {
      throw new IllegalArgumentException("주소는 null이거나 공백일 수 없습니다.");
    }
    return new Address(
        address, Objects.requireNonNull(latitude), Objects.requireNonNull(longitude));
  }
}
