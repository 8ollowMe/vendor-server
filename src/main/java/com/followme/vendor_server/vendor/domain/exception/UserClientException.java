package com.followMe.vendor_server.vendor.domain.exception;

import com.followMe.common.exception.BusinessException;
import com.followMe.common.exception.ErrorCode;

public class UserClientException extends BusinessException {

  public UserClientException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserClientException(ErrorCode errorCode, String detailMessage) {
    super(errorCode, detailMessage);
  }

  public UserClientException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
