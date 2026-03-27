package com.followme.vendor_server.vendor.domain;

import com.followMe.common.entity.BaseAudit;
import com.followme.vendor_server.vendor.domain.vo.ProductId;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@ToString
@Getter
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAudit {
  @EmbeddedId private ProductId id;

  @ManyToOne
  @JoinColumn(name = "vendor_id")
  private Vendor vendor;

  @Column(nullable = false)
  private UUID hubId;

  @Column(length = 50, nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @Builder(access = AccessLevel.PRIVATE)
  private Product(
      UUID id,
      Vendor vendor,
      UUID hubId,
      String code,
      String name,
      String description,
      Integer price,
      ProductStatus status) {
    this.id = id == null ? ProductId.of() : ProductId.of(id);
    this.vendor = vendor;
    this.hubId = hubId;
    this.code = code;
    this.name = name;
    this.description = description;
    this.price = price;
    this.status = status;
  }
}
