package com.followMe.vendor_server.vendor.domain;

import com.followMe.common.entity.BaseAudit;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followMe.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followMe.vendor_server.vendor.domain.service.ProductPermissionChecker;
import com.followMe.vendor_server.vendor.domain.vo.ProductId;
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
 *       ProductPermissionChecker, HubExistenceChecker, ProductCodeValidator)} - 상품 등록
 *   <li>{@link #updateInfo(UUID, UUID, UUID, String, String, Integer, String, HubExistenceChecker,
 *       ProductPermissionChecker, ProductCodeValidator)} - 상품 정보 수정
 * </ul>
 *
 * @author 정승현
 */
@Entity
@Getter
@Table(
    name = "p_product",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_vendor_product_code",
          columnNames = {"vendor_id", "code"})
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAudit {

  @EmbeddedId private ProductId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vendor_id", nullable = false)
  private Vendor vendor;

  @Column(nullable = false)
  private UUID hubId;

  @Column(length = 50, nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  /**
   * 상품의 고유 식별자를 UUID 형태로 반환한다.
   *
   * @return 상품 UUID
   * @author 정승현
   */
  public UUID toUuid() {
    return this.getId().getId();
  }

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
   *   <li>상품 코드 중복 검증
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
  protected static Product create(
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
      HubExistenceChecker hubExistenceChecker,
      ProductCodeValidator productCodeValidator) {

    checkValidPrice(price);
    checkValidProductCode(vendor.toUuid(), code, productCodeValidator);
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

  /**
   * 상품 정보를 수정한다.
   *
   * <h2>상품 수정 검증</h2>
   *
   * <ul>
   *   <li>상품 가격 유효성 검증
   *   <li>소속 허브 존재 여부 검증
   *   <li>상품 수정 권한 검증 [MASTER, HUB(담당 허브), VENDOR(본인 업체)]
   *   <li>상품 코드 수정시, 상품 코드 중복 검증
   * </ul>
   *
   * @param hubId 허브 식별자
   * @param ownerId 업체 대표 식별자
   * @param requesterId 수정 요청자 식별자
   * @param code 수정할 상품 코드
   * @param name 수정할 상품명
   * @param price 수정할 가격
   * @param description 수정할 설명
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @param productPermissionChecker 권한 검증 인터페이스
   * @param productCodeValidator 상품 코드 중복 검증 인터페이스
   * @return 수정된 상품 엔티티
   * @throws VendorException 검증 실패 시 발생
   * @author 정승현
   */
  protected Product updateInfo(
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      String code,
      String name,
      Integer price,
      String description,
      HubExistenceChecker hubExistenceChecker,
      ProductPermissionChecker productPermissionChecker,
      ProductCodeValidator productCodeValidator) {

    checkValidPrice(price);
    checkHubExistence(hubId, hubExistenceChecker);
    checkUpdateProductPermission(hubId, ownerId, requesterId, productPermissionChecker);

    if (!this.code.equals(code)) {
      checkValidProductCode(vendor.toUuid(), code, productCodeValidator);
      this.code = code;
    }

    this.name = name;
    this.description = description;
    this.price = price;

    return this;
  }

  protected Product updateStatus(
      UUID hubId,
      UUID requesterId,
      UUID ownerId,
      ProductStatus status,
      HubExistenceChecker hubExistenceChecker,
      ProductPermissionChecker productPermissionChecker) {

    checkHubExistence(hubId, hubExistenceChecker);
    checkUpdateProductPermission(hubId, ownerId, requesterId, productPermissionChecker);
    this.status = status;
    return this;
  }

  /**
   * 상품을 삭제한다.
   *
   * <p><o>상품삭제는 반드시 업체를 통해서 이루어져야 한다.
   *
   * <h2>상품 삭제 검증</h2>
   *
   * <ul>
   *   <li>상품 삭제 권한 검증
   * </ul>
   *
   * @param hubId 업체가 소속된 허브
   * @param ownerId 업체의 대표 식별자
   * @param requesterId 삭제 요청을 한 사용자 식별자
   * @param productPermissionChecker 상품 권한 검증 인터페이스
   */
  protected void delete(
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      ProductPermissionChecker productPermissionChecker) {
    checkDeleteProductPermission(hubId, ownerId, requesterId, productPermissionChecker);
    this.softDelete();
  }

  /**
   * 업체가 삭제되면, 업체가 등록한 상품도 같이 삭제 된다.
   *
   * <p>업체를 삭제하는 행위는 업체에서 유효성 검사를 하기때문에 상품 도메인에서는 추가 검증을 하지않는다.
   */
  protected void cascadeDelete() {
    this.softDelete();
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

  private void checkUpdateProductPermission(
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      ProductPermissionChecker productPermissionChecker) {
    if (!productPermissionChecker.hasUpdatePermission(hubId, ownerId, requesterId)) {
      throw new VendorException(VendorErrorCode.PRODUCT_UPDATE_FORBIDDEN);
    }
  }

  private void checkDeleteProductPermission(
      UUID hubId,
      UUID ownerId,
      UUID requesterId,
      ProductPermissionChecker productPermissionChecker) {
    if (!productPermissionChecker.hasDeletePermission(hubId, ownerId, requesterId)) {
      throw new VendorException(VendorErrorCode.PRODUCT_DELETE_FORBIDDEN);
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

  private static void checkValidProductCode(
      UUID vendorId, String code, ProductCodeValidator productCodeValidator) {
    if (productCodeValidator.isCodeDuplicated(vendorId, code)) {
      throw new VendorException(VendorErrorCode.PRODUCT_DUPLICATE_CODE);
    }
  }
}
