package aug.bueno.product.microservice.repository;

import aug.bueno.product.microservice.domain.Product;
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

    void testFindAll(){
        List<Product> products = repository.findAll();
        Assertions.assertEquals(2, products.size());
    }

}
