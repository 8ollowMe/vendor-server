package com.followme.vendor_server.global;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

  // TODO: UUID로 변경해야 됨
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("12456");
  }
}
