package com.followMe.vendor_server.vendor.domain.event;

import lombok.Getter;

@Getter
public enum DomainTypes {
  VENDOR("VENDOR"),
  PRODUCT("PRODUCT");

  public final String type;

  DomainTypes(String type) {
    this.type = type;
  }
}
