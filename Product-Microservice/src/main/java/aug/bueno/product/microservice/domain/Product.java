package aug.bueno.product.microservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    long id;

    String name;

    Integer quantity;

    Integer version;

    public Product(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
