package aug.bueno.product.microservice.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    long id;

    Integer version;

    String name;

    Integer quantity;

    public ProductDTO(String productName, int quantity) {
        this.name = productName;
        this.quantity = quantity;
    }
}
