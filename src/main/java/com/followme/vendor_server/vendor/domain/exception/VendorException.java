package com.followme.vendor_server.vendor.domain.exception;

import com.followMe.common.exception.BusinessException;

/**
 * 업체관련 예외 처리
 *
 * <p>프로젝트에서 공통으로 사용할 라이브러리를 상속받아 정의
 *
 * @author 정승현
 */
public class VendorException extends BusinessException {

  public VendorException(VendorErrorCode errorCode) {
    super(errorCode);
  }

  public VendorException(VendorErrorCode errorCode, String detailMessage) {
    super(errorCode, detailMessage);
  }

  public VendorException(VendorErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
