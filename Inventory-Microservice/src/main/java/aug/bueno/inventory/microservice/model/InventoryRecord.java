package aug.bueno.inventory.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRecord implements Serializable {

    private Long productId;

    private Integer quantity;

    private String productName;

    private String productCategory;

}
