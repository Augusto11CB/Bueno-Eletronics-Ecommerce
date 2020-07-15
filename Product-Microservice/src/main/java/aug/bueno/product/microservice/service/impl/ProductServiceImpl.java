package aug.bueno.product.microservice.service.impl;

import aug.bueno.product.microservice.domain.dto.ProductDTO;
import aug.bueno.product.microservice.repository.ProductRepository;
import aug.bueno.product.microservice.service.ProductService;

import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProductDTO> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<ProductDTO> findAll() {
        return null;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        return null;
    }

    @Override
    public boolean update(ProductDTO productDTO) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
