package aug.bueno.product.microservice.repository.impl;

import aug.bueno.product.microservice.controller.ProductController;
import aug.bueno.product.microservice.domain.Product;
import aug.bueno.product.microservice.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("products")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Product> findById(Integer id) {

        try {
            Product product = jdbcTemplate.queryForObject(
                    "SELECT * FROM products WHERE id = ?",
                    new Object[]{id},
                    (rs, rowNum) -> {
                        Product p = new Product();
                        p.setId(rs.getLong("id"));
                        p.setName(rs.getString("name"));
                        p.setQuantity(rs.getInt("quantity"));
                        p.setVersion(rs.getInt("version"));
                        return p;
                    });

            return Optional.of(product);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM products",
                (rs, rowNum) -> {
                    Product p = new Product();
                    p.setId(rs.getLong("id"));
                    p.setName(rs.getString("name"));
                    p.setQuantity(rs.getInt("quantity"));
                    p.setVersion(rs.getInt("version"));
                    return p;
                });
    }

    @Override
    public boolean update(Product product) {

        return jdbcTemplate.update(
                "UPDATE products SET name = ?, quantity = ?, version = ? WHERE id = ?",
                product.getName(),
                product.getQuantity(),
                product.getVersion(),
                product.getVersion(),
                product.getId()) == 1;
    }

    @Override
    public Product save(Product product) {

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", product.getName());
        parameters.put("quantity", product.getQuantity());
        parameters.put("version", product.getVersion());

        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);

        LOGGER.info("Inserting product into database, generated key is: {}", newId);

        product.setId((Long) newId);

        return product;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update("DELETE FROM products WHERE id = ?", id) == 1;
    }
}
