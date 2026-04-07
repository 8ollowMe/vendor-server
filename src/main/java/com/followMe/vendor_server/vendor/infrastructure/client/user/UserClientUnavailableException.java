package com.followMe.vendor_server.vendor.infrastructure.client.user;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;

public class UserClientUnavailableException extends VendorException {
  public UserClientUnavailableException() {
    super(VendorErrorCode.USER_CLIENT_UNAVAILABLE);
  }
}
