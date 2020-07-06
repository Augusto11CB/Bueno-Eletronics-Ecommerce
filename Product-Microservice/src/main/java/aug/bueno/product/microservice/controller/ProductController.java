package aug.bueno.product.microservice.controller;

import aug.bueno.product.microservice.domain.dto.ProductDTO;
import aug.bueno.product.microservice.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
public class ProductController {

    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {

        return productService.findById(id)
                .map(product -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(product.getVersion()))
                                .location(new URI("/product/" + product.getId()))
                                .body(product);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }

                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products")
    public Iterable<ProductDTO> getProducts() {
        return productService.findAll();
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        LOGGER.info("Creating new product with name: {}, quantity:{}", productDTO.getName(), productDTO.getQuantity());

        ProductDTO newProduct = productService.save(productDTO);

        return ResponseEntity
                .created(URI.create("/product/" + newProduct.getId()))
//                .created(new URI("/product/" + newProduct.getId()))
                .eTag(Integer.toString(newProduct.getVersion()))
                .body(newProduct);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @RequestBody ProductDTO productUpdatedDTO,
            @PathVariable Integer id,
            @RequestHeader("If-Match") Integer ifMatch
    ) {
        LOGGER.info("Updating product with id: {}, name: {}, quantity: {}", id, productUpdatedDTO.getName(), productUpdatedDTO.getQuantity());

        Optional<ProductDTO> product = productService.findById(id);

        return product.map(p -> {

            if (!p.getVersion().equals(ifMatch)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            p.setName(productUpdatedDTO.getName());
            p.setQuantity(productUpdatedDTO.getQuantity());
            p.setVersion(p.getVersion() + 1);

            if (productService.update(p)) {
                return ResponseEntity
                        .ok()
                        .location(URI.create("/product/" + p.getId()))
                        .eTag(Integer.toString(p.getVersion()))
                        .body(p);
            } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        }).orElse(ResponseEntity.notFound().build());
    }
}
