package com.followme.vendor_server.vendor.domain;

import com.followMe.common.entity.BaseAudit;
import com.followMe.common.exception.ErrorCode;
import com.followme.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followme.vendor_server.vendor.domain.exception.VendorException;
import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followme.vendor_server.vendor.domain.service.PermissionChecker;
import com.followme.vendor_server.vendor.domain.service.ProductPermissionChecker;
import com.followme.vendor_server.vendor.domain.vo.ProductId;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * 상품(Product) 엔티티
 *
 * <p>루트 애그리거트인 업체(Vendor)를 통해 접근해야 하며, 상품을 직접 조작하는것은 지양해야 한다.
 *
 * <p>특정 업체(Vendor)에 소속되며, 관리되는 허브(Hub) 정보를 포함한다.
 *
 * <p>권한 검증 및 허브 존재 여부 확인을 위해 {@link ProductPermissionChecker}, {@link HubExistenceChecker}를 사용한다.
 *
 * <p>등록, 수정, 삭제 시에는 각 도메인 규칙에 따라 권한과 허브 존재 여부가 검증되야하며, 검증 실패시 {@link VendorException} 또는 {@link
 * IllegalArgumentException} 이 발생한다.
 *
 * <h2>필드 정보</h2>
 *
 * <ul>
 *   <li>id - 상품 식별자 {@link ProductId}
 *   <li>vendor - 소속 업체 {@link Vendor}
 *   <li>hubId - 소속 허브 식별자
 *   <li>code - 상품 식별 코드
 *   <li>name - 상품 이름
 *   <li>price - 상품 가격
 *   <li>description - 상품에 대한 설명
 *   <li>status - 상품 상태 {@link ProductStatus}
 * </ul>
 *
 * <h2>주요 메서드</h2>
 *
 * <ul>
 *   <li>{@link #create(Vendor, UUID, UUID, UUID, String, String, String, Integer, ProductStatus,
 *       ProductPermissionChecker, HubExistenceChecker)} - 상품 등록
 * </ul>
 */
@Entity
@Getter
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAudit {

  @EmbeddedId private ProductId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vendor_id", nullable = false)
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

  /**
   * 상품 생성 팩토리 메서드
   *
   * <p>상품을 등록한다.
   *
   * <h2>상품 등록 검증</h2>
   *
   * <ul>
   *   <li>상품 가격 유효성 검증
   *   <li>상품 생성 권한 검증 [MASTER, HUB(담당 허브), VENDOR(본인 업체)]
   *   <li>소속 허브 존재 여부 검증
   * </ul>
   *
   * @param vendor 상품을 등록할 업체 엔티티
   * @param hubId 상품이 관리될 허브 식별자
   * @param ownerId 업체 대표 사용자 식별자
   * @param requesterId 권한 검증을 요청한 사용자 식별자
   * @param code 상품 코드
   * @param name 상품 명
   * @param description 상품 설명
   * @param price 상품 가격
   * @param status 상품 상태 [ON_SALE, SOLD_OUT, DISCONTINUED, PENDING]
   * @param productPermissionChecker 상품 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @return 등록된 상품 엔티티
   * @throws VendorException 권한 부족 또는 허브 미존재 시 발생
   * @throws IllegalArgumentException 잘못된 인자로 생성 요청 시 발생
   * @author 정승현
   */
  public static Product create(
      Vendor vendor,
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      String code,
      String name,
      String description,
      Integer price,
      ProductStatus status,
      ProductPermissionChecker productPermissionChecker,
      HubExistenceChecker hubExistenceChecker) {

    checkValidPrice(price);
    checkCreateProductPermission(hubId, ownerId, requesterId, productPermissionChecker);
    checkHubExistence(hubId, hubExistenceChecker);

    return Product.builder()
        .vendor(vendor)
        .hubId(hubId)
        .code(code)
        .name(name)
        .description(description)
        .price(price)
        .status(status)
        .build();
  }

  private static void checkCreateProductPermission(
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      ProductPermissionChecker productPermissionChecker) {
    if (!productPermissionChecker.hasCreatePermission(hubId, ownerId, requesterId)) {
      throw new VendorException(VendorErrorCode.PRODUCT_REGISTER_FORBIDDEN);
    }
  }

  private static void checkHubExistence(UUID hubId, HubExistenceChecker hubExistenceChecker) {
    if (!hubExistenceChecker.hasHub(hubId)) {
      throw new VendorException(VendorErrorCode.HUB_NOT_FOUND);
    }
  }

  private static void checkValidPrice(Integer price) {
    if (price < 0) {
      throw new VendorException(VendorErrorCode.PRODUCT_INVALID_PRICE);
    }
  }
}
