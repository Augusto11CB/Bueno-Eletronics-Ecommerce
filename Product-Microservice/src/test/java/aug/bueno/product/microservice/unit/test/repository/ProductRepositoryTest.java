package aug.bueno.product.microservice.unit.test.repository;

import aug.bueno.product.microservice.domain.Product;
import aug.bueno.product.microservice.repository.ProductRepository;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith({SpringExtension.class, DBUnitExtension.class})
@ActiveProfiles("test")
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository repository;

    public ConnectionHolder getConnectionHolder() throws SQLException {
        // RETURNS A FUNCTION THAT RETRIEVES A CONNECTION FROM OUR DATA SOURCE
        return () -> dataSource.getConnection();
    }


    @Test
    @DataSet("products.yml")
    void testFindAll() {
        List<Product> products = repository.findAll();
        Assertions.assertEquals(2, products.size());
    }

    @Test
    @DataSet("products.yml")
    void testFindByIdSuccess() {
        Optional<Product> byId = repository.findById(2L);

        assertTrue(byId.isPresent());

        Product product = byId.get();

        assertEquals(2L, product.getId());
        assertEquals(5, product.getQuantity());
        assertEquals(2, product.getVersion());
    }

    @Test
    @DataSet("products.yml")
    void testFindByIdNotFound() {
        Optional<Product> byId = repository.findById(3L);

        assertFalse(byId.isPresent());
    }


    @Test
    @DataSet("products.yml")
    void testSave() {
        Product product = new Product("P5", 5);
        product.setVersion(1);
        Product saveProduct = repository.save(product);

        assertEquals(product.getName(), saveProduct.getName());
        assertEquals(product.getQuantity(), saveProduct.getQuantity());

        Optional<Product> loadedProductOP = repository.findById(saveProduct.getId());

        assertTrue(loadedProductOP.isPresent());

        Product loadedProduct = loadedProductOP.get();

        assertEquals("P5", loadedProduct.getName());
        assertEquals(5, loadedProduct.getQuantity().intValue());
        assertEquals(1, loadedProduct.getVersion().intValue());
    }

    @Test
    @DataSet("products.yml")
    void testUpdateSucess() {
        Product product = new Product(1,"P1", 100,5);
        boolean result = repository.update(product);

        assertTrue(result);

        Optional<Product> loadedProductOP = repository.findById(product.getId());

        assertTrue(loadedProductOP.isPresent());

        Product loadedProduct = loadedProductOP.get();

        assertEquals("P1", loadedProduct.getName());
        assertEquals(100, loadedProduct.getQuantity().intValue());
        assertEquals(5, loadedProduct.getVersion().intValue());
    }

    @Test
    @DataSet("products.yml")
    void testUpdateFailure() {
        Product product = new Product(3,"P3", 100,5);
        boolean result = repository.update(product);

        assertFalse(result);

    }

    @Test
    @DataSet("products.yml")
    void testDeleteSucess() {
        boolean result = repository.delete(1L);

        assertTrue(result);

        Optional<Product> loadedProductOP = repository.findById(1L);

        assertFalse(loadedProductOP.isPresent());

    }

    @Test
    @DataSet("products.yml")
    void testDeleteFailure() {
        boolean result = repository.delete(3L);

        assertFalse(result);

    }

}
