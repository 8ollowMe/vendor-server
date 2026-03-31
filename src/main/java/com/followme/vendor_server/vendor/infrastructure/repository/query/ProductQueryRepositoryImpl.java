package com.followMe.vendor_server.vendor.infrastructure.repository.query;

import com.followMe.vendor_server.vendor.application.dto.ProductDetailResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductSummaryResponse;
import com.followMe.vendor_server.vendor.application.query.ProductSearchCondition;
import com.followMe.vendor_server.vendor.domain.ProductStatus;
import com.followMe.vendor_server.vendor.domain.QProduct;
import com.followMe.vendor_server.vendor.domain.QVendor;
import com.followMe.vendor_server.vendor.domain.query.ProductQueryRepository;
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
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ProductSummaryResponse> searchProducts(
      ProductSearchCondition condition, Pageable pageable) {
    QProduct product = QProduct.product;

    List<ProductSummaryResponse> content =
        queryFactory
            .select(productSummaryResponse(product))
            .from(product)
            .where(
                vendorIdEq(condition.getVendorId(), product),
                nameContains(condition.getName(), product),
                hubIdEq(condition.getHubId(), product),
                codeEq(condition.getCode(), product),
                statusEq(condition.getStatus(), product))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(product.createdAt.desc())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(product.count())
            .from(product)
            .where(
                vendorIdEq(condition.getVendorId(), product),
                nameContains(condition.getName(), product),
                hubIdEq(condition.getHubId(), product),
                codeEq(condition.getCode(), product),
                statusEq(condition.getStatus(), product));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  @Override
  public Optional<ProductDetailResponse> findProductDetailById(UUID productId) {
    QProduct product = QProduct.product;
    QVendor vendor = QVendor.vendor;

    return Optional.ofNullable(
        queryFactory
            .select(productDetailResponse(product))
            .from(product)
            .join(product.vendor, vendor)
            .where(product.id.id.eq(productId))
            .fetchOne());
  }

  private QBean<ProductDetailResponse> productDetailResponse(QProduct product) {
    return Projections.fields(
        ProductDetailResponse.class,
        product.id.id.as("productId"),
        product.name.as("name"),
        product.price.as("price"),
        product.code.as("code"),
        product.description.as("description"),
        product.status.as("status"),
        product.hubId.as("hubId"),
        product.vendor.id.id.as("vendorId"),
        product.vendor.name.as("vendorName"));
  }

  private QBean<ProductSummaryResponse> productSummaryResponse(QProduct product) {
    return Projections.fields(
        ProductSummaryResponse.class,
        product.id.id.as("productId"),
        product.name.as("name"),
        product.price.as("price"),
        product.code.as("code"),
        product.status.as("status"),
        product.hubId.as("hubId"),
        product.vendor.id.id.as("vendorId"));
  }

  private BooleanExpression vendorIdEq(UUID vendorId, QProduct product) {
    return vendorId != null ? product.vendor.id.id.eq(vendorId) : null;
  }

  private BooleanExpression nameContains(String name, QProduct product) {
    return StringUtils.hasText(name) ? product.name.contains(name) : null;
  }

  private BooleanExpression hubIdEq(UUID hubId, QProduct product) {
    return hubId != null ? product.hubId.eq(hubId) : null;
  }

  private BooleanExpression codeEq(String code, QProduct product) {
    return StringUtils.hasText(code) ? product.code.eq(code) : null;
  }

  private BooleanExpression statusEq(ProductStatus status, QProduct product) {
    return status != null ? product.status.eq(status) : null;
  }
}
