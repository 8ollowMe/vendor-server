package com.followMe.vendor_server.vendor.domain.exception;

import com.followMe.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 업체 관련 오류 코드 정의
 *
 * <p>업체 도메인 내에서 사용되는 에러 코드를 정의.
 *
 * <p>프로젝트 공통 라이브러리 {@code ErrorCode} 인터페이스를 통해 구현
 *
 * <ul>
 *   <li>{@link #code} - 에러를 분류하기위한 고유 코드
 *   <li>{@link #message} - 에러를 안내할 기본 메시지
 *   <li>{@link #httpStatus} - HTTP 표준 상태
 * </ul>
 *
 * <p>{@link #code}
 *
 * <p>{@code V001} ~ {@code V100} - 업체 관련 에러
 *
 * <p>{@code V101} ~ {@code V200} - 허브 관련 에러
 *
 * <p>{@code V401} ~ {@code V200} - 권한 관련 에러
 *
 * @author 정승현
 */
@RequiredArgsConstructor
public enum VendorErrorCode implements ErrorCode {
  VENDOR_NOT_FOUND("V001", "존재하지 않는 업체 입니다.", HttpStatus.NOT_FOUND),

  PRODUCT_INVALID_PRICE("V050", "유효하지 않은 상품 가격입니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_DUPLICATE_CODE("v051", "중복되는 상품 코드가 존재합니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_NOT_FOUND("v052", "해당 업체에 존재하지 않는 상품 입니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_DUPLICATE_UPDATE_REQUEST(
      "v053", "한 번의 요청에 동일한 상품의 상태 수정이 요청되었습니다.", HttpStatus.BAD_REQUEST),

  HUB_NOT_FOUND("V101", "존재하지 않는 허브입니다.", HttpStatus.NOT_FOUND),

  USER_INVALID_TYPE("V301", "옳바르지않은 유저 타입입니다.", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("302", "존재하지않는 유저 입니다.", HttpStatus.NOT_FOUND),

  /** 권한 관련 에러 */
  VENDOR_REGISTER_FORBIDDEN("V401", "업체 등록 권한이 부족합니다.", HttpStatus.FORBIDDEN),
  PRODUCT_REGISTER_FORBIDDEN("V402", "상품 등록 권한이 부족합니다.", HttpStatus.FORBIDDEN),
  VENDOR_UPDATE_FORBIDDEN("V403", "업체 수정 권한이 부족합니다.", HttpStatus.FORBIDDEN),
  PRODUCT_UPDATE_FORBIDDEN("V404", "상품 수정 권한이 부족합니다.", HttpStatus.FORBIDDEN),
  VENDOR_DELETE_FORBIDDEN("405", "업체 삭제 권한이 부족합니다.", HttpStatus.FORBIDDEN),
  PRODUCT_DELETE_FORBIDDEN("406", "상품 삭제 권한이 부족합니다.", HttpStatus.FORBIDDEN),

  VENDOR_INTERNAL_ERROR("v501", "알수없는 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }
}
