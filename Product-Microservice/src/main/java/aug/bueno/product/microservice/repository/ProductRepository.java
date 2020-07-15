package aug.bueno.product.microservice.repository;

import aug.bueno.product.microservice.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
