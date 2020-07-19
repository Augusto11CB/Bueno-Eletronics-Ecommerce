package aug.bueno.inventory.microservice.service;

import aug.bueno.inventory.microservice.model.InventoryRecord;

import java.util.Optional;

public interface InventoryService {

    Optional<InventoryRecord> getInventoryRecord(Long id);

    Optional<InventoryRecord> purchaseProduct(Long productId, Integer quantityPurchased);
}
