package com.followMe.vendor_server.vendor.infrastructure.repository.query;

import com.followMe.vendor_server.vendor.application.dto.VendorDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.VendorSummaryResponse;
import com.followMe.vendor_server.vendor.application.query.VendorSearchCondition;
import com.followMe.vendor_server.vendor.domain.QVendor;
import com.followMe.vendor_server.vendor.domain.VendorType;
import com.followMe.vendor_server.vendor.domain.query.VendorQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class VendorQueryRepositoryImpl implements VendorQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<VendorSummaryResponse> searchVendors(
      VendorSearchCondition condition, Pageable pageable) {
    QVendor vendor = QVendor.vendor;

    List<VendorSummaryResponse> content =
        queryFactory
            .select(vendorSummaryResponse(vendor))
            .from(vendor)
            .where(
                nameContains(condition.getName(), vendor),
                typeEq(condition.getType(), vendor),
                hubIdEq(condition.getHubId(), vendor))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(vendor.createdAt.desc())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(vendor.count())
            .from(vendor)
            .where(
                nameContains(condition.getName(), vendor),
                typeEq(condition.getType(), vendor),
                hubIdEq(condition.getHubId(), vendor));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  @Override
  public Optional<VendorDetailResponse> findVendorDetailById(UUID vendorId) {
    QVendor vendor = QVendor.vendor;

    return Optional.ofNullable(
        queryFactory
            .select(vendorDetailResponse(vendor))
            .from(vendor)
            .where(vendor.id.id.eq(vendorId))
            .fetchOne());
  }

  private QBean<VendorDetailResponse> vendorDetailResponse(QVendor vendor) {
    return Projections.fields(
        VendorDetailResponse.class,
        vendor.id.id.as("vendorId"),
        vendor.name.as("name"),
        vendor.type.as("type"),
        vendor.description.as("description"),
        vendor.hubId.as("hubId"),
        vendor.owner.name.as("ownerName"),
        vendor.address.address.as("address"),
        vendor.address.latitude.as("latitude"),
        vendor.address.longitude.as("longitude"));
  }

  private QBean<VendorSummaryResponse> vendorSummaryResponse(QVendor vendor) {
    return Projections.fields(
        VendorSummaryResponse.class,
        vendor.id.id.as("vendorId"),
        vendor.name.as("name"),
        vendor.type.as("type"),
        vendor.hubId.as("hubId"),
        vendor.owner.name.as("ownerName"),
        vendor.address.address.as("address"));
  }

  private BooleanExpression nameContains(String name, QVendor vendor) {
    return StringUtils.hasText(name) ? vendor.name.contains(name) : null;
  }

  private BooleanExpression typeEq(VendorType type, QVendor vendor) {
    return type != null ? vendor.type.eq(type) : null;
  }

  private BooleanExpression hubIdEq(UUID hubId, QVendor vendor) {
    return hubId != null ? vendor.hubId.eq(hubId) : null;
  }
}
