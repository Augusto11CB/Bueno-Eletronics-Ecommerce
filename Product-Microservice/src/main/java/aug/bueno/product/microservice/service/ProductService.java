package aug.bueno.product.microservice.service;

import aug.bueno.product.microservice.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(long id);

    List<Product> findAll();

    Product save(Product product);

    boolean update(Product product);

    boolean delete(long id);
}
