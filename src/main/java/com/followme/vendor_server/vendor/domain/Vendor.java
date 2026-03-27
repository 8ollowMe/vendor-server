package com.followme.vendor_server.vendor.domain;

import com.followMe.common.entity.BaseAudit;
import com.followMe.common.exception.BusinessException;
import com.followme.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followme.vendor_server.vendor.domain.exception.VendorException;
import com.followme.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followme.vendor_server.vendor.domain.service.PermissionChecker;
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
 *   <li>{@link #create(UUID, Owner, String, VendorType, String, Address, PermissionChecker,
 *       HubExistenceChecker)} - 업체 등록
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
   * <p>권한 검증 실패 시 {@link BusinessException} 발생
   *
   * <p>잘못된 인자로 생성 요청 시 {@link IllegalArgumentException} 발생
   *
   * <ul>
   *   <li>생성 권한 검증 [MASTER, HUB]
   *   <li>허브 존재 여부 검증
   * </ul>
   *
   * @param hubId 소속된 허브 Id
   * @param requestId 업체 등록 요청자 Id
   * @param name 업체를 등록 할 이름
   * @param type 업체 종류 [SUPPLIER, BUYER]
   * @param description 업체에 대한 설명
   * @param address 업체의 주소정보
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @return 등록된 업체 엔티티
   * @throws BusinessException 비즈니스 로직 에러
   * @throws IllegalArgumentException 잘못된 인자 에러
   * @author 정승현
   */
  public static Vendor create(
      UUID hubId,
      Owner requestId,
      String name,
      VendorType type,
      String description,
      Address address,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker) {

    checkCreatePermission(hubId, requestId.getId(), permissionChecker);
    checkHubExistence(hubId, hubExistenceChecker);

    return Vendor.builder()
        .hubId(hubId)
        .owner(requestId)
        .name(name)
        .type(type)
        .description(description)
        .address(address)
        .build();
  }

  private static void checkCreatePermission(
      UUID hubId, UUID requestId, PermissionChecker permissionChecker) {
    if (!permissionChecker.hasCreatePermission(hubId, requestId)) {
      throw new VendorException(VendorErrorCode.VENDOR_REGISTER_FORBIDDEN);
    }
  }

  private static void checkHubExistence(UUID hubId, HubExistenceChecker hubExistenceChecker) {
    if (!hubExistenceChecker.hasHub(hubId)) {
      throw new VendorException(VendorErrorCode.HUB_NOT_FOUND);
    }
  }
}
