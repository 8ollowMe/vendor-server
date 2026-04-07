package com.followMe.vendor_server.vendor.application.product;

import static com.followMe.vendor_server.vendor.application.command.BulkUpdateProductStatusCommand.ProductStatusUpdateItem;

import com.followMe.vendor_server.vendor.application.command.BulkUpdateProductStatusCommand;
import com.followMe.vendor_server.vendor.application.command.UpdateProductCommand;
import com.followMe.vendor_server.vendor.application.command.UpdateProductStatusCommand;
import com.followMe.vendor_server.vendor.application.dto.ProductStatusUpdateDto.ProductStatusUpdateResponse;
import com.followMe.vendor_server.vendor.application.dto.ProductUpdateDto.ProductUpdateResponse;
import com.followMe.vendor_server.vendor.domain.Product;
import com.followMe.vendor_server.vendor.domain.Vendor;
import com.followMe.vendor_server.vendor.domain.VendorRepository;
import com.followMe.vendor_server.vendor.domain.event.ProductEvents;
import com.followMe.vendor_server.vendor.domain.exception.VendorErrorCode;
import com.followMe.vendor_server.vendor.domain.exception.VendorException;
import com.followMe.vendor_server.vendor.domain.service.HubExistenceChecker;
import com.followMe.vendor_server.vendor.domain.service.ProductCodeValidator;
import com.followMe.vendor_server.vendor.domain.service.ProductPermissionChecker;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProductService {
  private final VendorRepository vendorRepository;
  private final HubExistenceChecker hubExistenceChecker;
  private final ProductPermissionChecker productPermissionChecker;
  private final ProductCodeValidator productCodeValidator;
  private final ProductEvents events;

  public ProductUpdateResponse updateProduct(UpdateProductCommand command) {
    Vendor vendor =
        vendorRepository
            .findByIdWithProducts(command.getVendorId())
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));

    Product product =
        vendor.updateProductInfo(
            command.getProductId(),
            command.getRequesterId(),
            command.getCode(),
            command.getName(),
            command.getPrice(),
            command.getDescription(),
            hubExistenceChecker,
            productPermissionChecker,
            productCodeValidator,
            events);

    /** TODO: updatedEvent 발행, outbox 를 하나의 트랜잭션으로 관리하는 기능 추가 해야 됨 */
    log.info("Updated product");
    return ProductUpdateResponse.from(product);
  }

  public ProductStatusUpdateResponse updateProductStatus(UpdateProductStatusCommand command) {
    Vendor vendor =
        vendorRepository
            .findByIdWithProducts(command.getVendorId())
            .orElseThrow(() -> new VendorException(VendorErrorCode.VENDOR_NOT_FOUND));

    Product product =
        vendor.updateProductStatus(
            command.getProductId(),
            command.getRequesterId(),
            command.getStatus(),
            hubExistenceChecker,
            productPermissionChecker);

    /** TODO: statusUpdatedEvent 발행 및 Outbox 저장 로직 추가 필요 */
    log.info("Updated product status");
    return ProductStatusUpdateResponse.from(product);
  }

  public void bulkUpdateProductStatus(BulkUpdateProductStatusCommand command) {

    checkDuplicateProductId(command.getItems());

    Map<UUID, List<ProductStatusUpdateItem>> itemsByVendor =
        command.getItems().stream()
            .collect(Collectors.groupingBy(ProductStatusUpdateItem::getVendorId));

    List<Vendor> vendors =
        vendorRepository.findAllByIdWithProducts(itemsByVendor.keySet()).stream().toList();

    for (Vendor vendor : vendors) {
      List<ProductStatusUpdateItem> vendorItems = itemsByVendor.get(vendor.toUuid());
      if (vendorItems == null) continue;

      for (ProductStatusUpdateItem item : vendorItems) {
        vendor.updateProductStatus(
            item.getProductId(),
            command.getRequesterId(),
            item.getStatus(),
            hubExistenceChecker,
            productPermissionChecker);
      }
    }
    log.info("Bulk updated Products");
  }

  private void checkDuplicateProductId(List<ProductStatusUpdateItem> items) {
    long uniqueCount = items.stream().map(ProductStatusUpdateItem::getProductId).distinct().count();

    if (uniqueCount != items.size()) {
      throw new VendorException(VendorErrorCode.PRODUCT_DUPLICATE_UPDATE_REQUEST);
    }
  }
}
