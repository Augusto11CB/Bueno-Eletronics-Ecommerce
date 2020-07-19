package aug.bueno.inventory.microservice.service.impl;

import aug.bueno.inventory.microservice.model.InventoryRecord;
import aug.bueno.inventory.microservice.model.PurchaseRecord;
import aug.bueno.inventory.microservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Value("${inventorymanager.baseUrl}")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<InventoryRecord> getInventoryRecord(Long id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(baseUrl + "/" + id, InventoryRecord.class));
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<InventoryRecord> purchaseProduct(Long id, Integer quantityPurchased) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(
                    baseUrl + "/" + id + "/purchaseRecord",
                    new PurchaseRecord(id, quantityPurchased),
                    InventoryRecord.class
            ));
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }

    }
}
