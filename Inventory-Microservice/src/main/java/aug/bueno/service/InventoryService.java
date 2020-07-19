package aug.bueno.service;

import aug.bueno.model.InventoryRecord;

import java.util.Optional;

public interface InventoryService {

    Optional<InventoryRecord> getInventoryRecord(Long id);

    Optional<InventoryRecord> purchaseProduct(Long productId, Integer quantityPurchased);
}
