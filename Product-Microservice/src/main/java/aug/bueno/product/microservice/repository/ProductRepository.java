package aug.bueno.product.microservice.repository;

import aug.bueno.product.microservice.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    List<Product> findAll();

    boolean update(Product product);

    Product save(Product product);

    boolean delete(Long id);
}
