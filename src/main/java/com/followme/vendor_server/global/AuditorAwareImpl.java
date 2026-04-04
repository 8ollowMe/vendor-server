package com.followMe.vendor_server.global;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<UUID> {

  // TODO: UUID로 변경해야 됨
  @Override
  public Optional<UUID> getCurrentAuditor() {
    return Optional.of(UUID.randomUUID());
  }
}
