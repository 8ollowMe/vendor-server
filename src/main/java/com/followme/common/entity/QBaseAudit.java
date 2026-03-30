package com.followMe.common.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import java.time.Instant;
import java.util.UUID;

public class QBaseAudit extends EntityPathBase<Object> {

  public static final QBaseAudit baseAudit = new QBaseAudit("baseAudit");

  public final DateTimePath<Instant> createdAt = createDateTime("createdAt", Instant.class);
  public final DateTimePath<Instant> updatedAt = createDateTime("updatedAt", Instant.class);
  public final DateTimePath<Instant> deletedAt = createDateTime("deletedAt", Instant.class);

  public final ComparablePath<UUID> createdBy = createComparable("createdBy", UUID.class);
  public final ComparablePath<UUID> updatedBy = createComparable("updatedBy", UUID.class);
  public final ComparablePath<UUID> deletedBy = createComparable("deletedBy", UUID.class);

  public QBaseAudit(String variable) {
    super(Object.class, variable);
  }

  public QBaseAudit(Path<? extends Object> path) {
    super(path.getType(), path.getMetadata());
  }
}
