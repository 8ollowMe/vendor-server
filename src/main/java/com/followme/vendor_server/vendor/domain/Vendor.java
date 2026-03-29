package com.followme.vendor_server.vendor.domain;

import com.followMe.common.entity.BaseAudit;
import com.followme.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followme.vendor_server.vendor.domain.exception.VendorException;
import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followme.vendor_server.vendor.domain.service.PermissionChecker;
import com.followme.vendor_server.vendor.domain.service.ProductPermissionChecker;
import com.followme.vendor_server.vendor.domain.vo.Address;
import com.followme.vendor_server.vendor.domain.vo.Owner;
import com.followme.vendor_server.vendor.domain.vo.VendorId;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

/**
 * 업체(Vendor) 도메인 엔티티
 *
 * <p>업체 등록, 조회, 정보 수정, 삭제 기능을 담당한다
 *
 * <p>권한 검증 및 허브 존재 여부 확인을 위해 {@link PermissionChecker}, {@link HubExistenceChecker}를 사용한다.
 *
 * <p>등록, 수정, 삭제 시에는 각 도메인 규칙에 따라 권한과 허브 존재 여부가 검증되야하며, 검증 실패시 {@link VendorException} 또는 {@link
 * IllegalArgumentException} 이 발생한다.
 *
 * <h2>필드 정보</h2>
 *
 * <ul>
 *   <li>id - 업체 식별자 {@link VendorId}
 *   <li>hubId - 소속 허브 식별자
 *   <li>owner - 업체 등록 대표 정보 {@link Owner}
 *   <li>name - 업체 이름
 *   <li>type - 업체 유형 {@link VendorType}
 *   <li>description - 업체에 대한 설명
 *   <li>address - 업체 위치 정보 {@link Address}
 *   <li>products - 업체의 상품 목록 {@link Product}
 * </ul>
 *
 * <h2>주요 메서드</h2>
 *
 * <ul>
 *   <li>{@link #create(UUID, UUID, String, String, VendorType, String, String, Double, Double,
 *       PermissionChecker, HubExistenceChecker)} - 업체 등록
 *   <li>{@link #updateInfo(UUID, UUID, String, VendorType, String, UUID, String, String, Double,
 *       Double, PermissionChecker, HubExistenceChecker)} - 업체 정보 수정
 * </ul>
 *
 * @author 정승현
 */
@Entity
@Getter
@Table(name = "p_vendor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor extends BaseAudit {

  @EmbeddedId private VendorId id;

  @Column(nullable = false)
  private UUID hubId;

  @Embedded private Owner owner;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private VendorType type;

  private String description;

  @Embedded private Address address;

  @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products = new ArrayList<>();

  @Builder(access = AccessLevel.PRIVATE)
  private Vendor(
      UUID id,
      UUID hubId,
      Owner owner,
      String name,
      VendorType type,
      String description,
      Address address) {
    this.id = id == null ? VendorId.of() : VendorId.of(id);
    this.hubId = Objects.requireNonNull(hubId);
    this.owner = owner;
    this.name = name;
    this.type = type;
    this.description = description;
    this.address = address;
  }

  /**
   * 업체 생성 팩토리 메서드
   *
   * <p>업체를 등록한다.
   *
   * <p>권한 검증 실패 시 {@link VendorException} 발생
   *
   * <p>잘못된 인자로 생성 요청 시 {@link IllegalArgumentException} 발생
   *
   * <h2>업체 등록 검증</h2>
   *
   * <ul>
   *   <li>생성 권한 검증 [MASTER, HUB]
   *   <li>허브 존재 여부 검증
   * </ul>
   *
   * @param hubId 소속된 허브 식별자
   * @param ownerId 업체 등록 요청자 식별자
   * @param ownerName 업체 등록 요청자 이름
   * @param name 업체를 등록 할 이름
   * @param type 업체 종류 [SUPPLIER, BUYER]
   * @param description 업체에 대한 설명
   * @param address 업체의 주소정보
   * @param longitude 업체 주소 경도 값
   * @param latitude 업체 주소 위도 값
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @return 등록된 업체 엔티티
   * @throws VendorException 비즈니스 로직 에러
   * @throws IllegalArgumentException 잘못된 인자 에러
   * @author 정승현
   */
  public static Vendor create(
      UUID hubId,
      UUID ownerId,
      String ownerName,
      String name,
      VendorType type,
      String description,
      String address,
      Double latitude,
      Double longitude,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker) {

    checkCreatePermission(hubId, ownerId, permissionChecker);
    checkHubExistence(hubId, hubExistenceChecker);

    return Vendor.builder()
        .hubId(hubId)
        .owner(Owner.of(ownerId, ownerName))
        .name(name)
        .type(type)
        .description(description)
        .address(Address.of(address, latitude, longitude))
        .build();
  }

  /**
   * 업체 정보를 수정 한다.
   *
   * <p>권한 검증 실패 시 {@link VendorException} 발생
   *
   * <p>잘못된 인자로 생성 요청 시 {@link IllegalArgumentException} 발생
   *
   * <h2>업체 수정 검증</h2>
   *
   * <ul>
   *   <li>허브 존재 유무 검증
   *   <li>업체 수정 권한 검증
   *   <li>업체 대표 수정 시, 수정하려는 유저 권한 검증
   * </ul>
   *
   * @param hubId 소속 허브 식별자
   * @param requestId 수정 요청을 한 사용자 식별자
   * @param name 수정할 업체 이름
   * @param type 수정할 업체 종류 [SUPPLIER, BUYER]
   * @param description 수정할 업체 설명
   * @param ownerId 수정할 대표 사용자 식별자
   * @param ownerName 수정할 대표 사용자 이름
   * @param address 수정할 업체 주소
   * @param latitude 수정할 주소 경도 값
   * @param longitude 수정할 주소 위도 값
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @throws VendorException 비즈니스 로직 에러
   * @throws IllegalArgumentException 잘못된 인자 에러
   * @author 정승현
   */
  public void updateInfo(
      UUID hubId,
      UUID requestId,
      String name,
      VendorType type,
      String description,
      UUID ownerId,
      String ownerName,
      String address,
      Double latitude,
      Double longitude,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker) {

    checkHubExistence(hubId, hubExistenceChecker);
    checkUpdatePermission(hubId, requestId, permissionChecker);

    if (!this.owner.getId().equals(ownerId)) {
      checkUpdatePermission(hubId, ownerId, permissionChecker);
      this.owner = Owner.of(ownerId, ownerName);
    }
    this.name = name;
    this.type = type;
    this.description = description;
    this.address = Address.of(address, latitude, longitude);
  }

  public Product addProduct(
      UUID requesterId,
      String code,
      String name,
      String description,
      Integer price,
      ProductStatus status,
      ProductPermissionChecker productPermissionChecker,
      HubExistenceChecker hubExistenceChecker) {

    Product product =
        Product.create(
            this,
            this.hubId,
            owner.getId(),
            requesterId,
            code,
            name,
            description,
            price,
            status,
            productPermissionChecker,
            hubExistenceChecker);
    this.products.add(product);
    return product;
  }

  private static void checkCreatePermission(
      UUID hubId, UUID requestId, PermissionChecker permissionChecker) {
    if (!permissionChecker.hasCreatePermission(hubId, requestId)) {
      throw new VendorException(VendorErrorCode.VENDOR_REGISTER_FORBIDDEN);
    }
  }

  private void checkUpdatePermission(
      UUID hubId, UUID requestId, PermissionChecker permissionChecker) {
    if (!permissionChecker.hasUpdatePermission(hubId, requestId)) {
      throw new VendorException(VendorErrorCode.VENDOR_UPDATE_FORBIDDEN);
    }
  }

  private static void checkHubExistence(UUID hubId, HubExistenceChecker hubExistenceChecker) {
    if (!hubExistenceChecker.hasHub(hubId)) {
      throw new VendorException(VendorErrorCode.HUB_NOT_FOUND);
    }
  }
}
