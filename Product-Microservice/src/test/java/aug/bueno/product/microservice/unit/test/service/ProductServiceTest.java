package aug.bueno.product.microservice.unit.test.service;

import aug.bueno.product.microservice.domain.Product;
import aug.bueno.product.microservice.repository.ProductRepository;
import aug.bueno.product.microservice.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;


    @Test
    @DisplayName("Test FindById Success")
    void testFindByIdSucess() {

        Product mockProduct = new Product(1, "Product Name", 10, 1);

        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        Optional<Product> returnedProduct = productService.findById(1L);

        assertTrue(returnedProduct.isPresent(), "Product was not found");
        assertSame(returnedProduct.get(), mockProduct, "Product was not found");
    }

    @Test
    @DisplayName("Test FindById Not Found")
    void testFindByIdNotFound() {

        Product mockProduct = new Product(1, "Product Name", 10, 1);

        doReturn(Optional.empty()).when(productRepository).findById(1L);

        Optional<Product> returnedProduct = productService.findById(1L);

        assertFalse(returnedProduct.isPresent(), "Product was not found");
    }

    @Test
    @DisplayName("Test FindAll")
    void testFindAll() {

        Product mockProduct = new Product(1, "Product Name", 10, 1);
        Product mockProduct2 = new Product(2, "Product Name 2", 15, 3);

        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productRepository).findAll();

        List<Product> returnedProduct = productService.findAll();

        assertEquals(2, returnedProduct.size(), "Product was not found");
    }

    @Test
    @DisplayName("Test save")
    void testSave() {

        Product mockProduct = new Product(1, "Product Name", 10, 1);

        doReturn(mockProduct).when(productRepository).save(any());

        Product returnedProduct = productService.save(mockProduct);

        assertNotNull(returnedProduct, "The saved product should not be null");
        assertEquals(1, returnedProduct.getVersion().intValue(), "The version for new product should be 1");
    }


}

