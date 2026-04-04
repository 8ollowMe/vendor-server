package com.followMe.vendor_server.vendor.application;

import com.followMe.vendor_server.vendor.domain.UserRole;
import java.util.UUID;

public record UserContext(UUID userId, UserRole role, String userName, UUID hubId, UUID vendorId) {}
