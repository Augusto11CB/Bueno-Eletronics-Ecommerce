//package aug.bueno.product.microservice.service;
//
//import aug.bueno.product.microservice.domain.Product;
//import aug.bueno.product.microservice.repository.ProductRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.doReturn;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class ProductServiceTest {
//
//    @Autowired
//    private ProductService productService;
//
//    @MockBean
//    private ProductRepository productRepository;
//
//
//    @Test
//    @DisplayName("Test FindById Success")
//    void testFindByIdSucess() {
//        aug.bueno.product.microservice.domain.Product mockProduct = new aug.bueno.product.microservice.domain.Product(1, "Product Name", 10, 1);
//        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);
//
//        Optional<Product> returnedProduct = productService.findById(1L);
//
//        assertTrue(returnedProduct.isPresent(),"Product was not found");
//    }
//}
