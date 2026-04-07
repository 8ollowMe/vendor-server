package com.followMe.vendor_server.vendor.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Owner {
  @Column(name = "owner_id", nullable = false)
  private UUID id;

  @Column(name = "owner_name", length = 50, nullable = false)
  private String name;

  public static Owner of(UUID id, String name) {
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException("이름은 null이거나 공백일 수 없습니다.");
    }
    return new Owner(Objects.requireNonNull(id), name);
  }
}
