package com.followMe.vendor_server.vendor.infrastructure.client.hub;

import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;

public class HubClientUnavailableException extends VendorException {
  public HubClientUnavailableException() {
    super(VendorErrorCode.HUB_CLIENT_UNAVAILABLE);
  }
}
