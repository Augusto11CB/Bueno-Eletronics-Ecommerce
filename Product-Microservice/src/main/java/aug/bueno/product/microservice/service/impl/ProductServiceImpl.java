package aug.bueno.product.microservice.service.impl;

import aug.bueno.product.microservice.domain.Product;
import aug.bueno.product.microservice.repository.ProductRepository;
import aug.bueno.product.microservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
