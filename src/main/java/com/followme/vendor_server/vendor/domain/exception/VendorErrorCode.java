package com.followme.vendor_server.vendor.domain.exception;

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
  HUB_NOT_FOUND("V101", "존재하지 않는 허브입니다.", HttpStatus.NOT_FOUND),

  VENDOR_REGISTER_FORBIDDEN("V401", "업체 등록 권한이 부족합니다.", HttpStatus.FORBIDDEN),
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
