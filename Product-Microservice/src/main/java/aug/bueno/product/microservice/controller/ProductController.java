package aug.bueno.product.microservice.controller;

import aug.bueno.product.microservice.domain.Product;
import aug.bueno.product.microservice.domain.dto.ProductDTO;
import aug.bueno.product.microservice.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class ProductController {

    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {

        return productService.findById(id)
                .map(product -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(product.getVersion()))
                                .location(new URI("/product/" + product.getId()))
                                .body(this.convertToDTO(product));
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }

                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products")
    public List<ProductDTO> getProducts() {
        return productService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody final ProductDTO productDTORequest) throws URISyntaxException {
        LOGGER.info("Creating new product with name: {}, quantity:{}", productDTORequest.getName(), productDTORequest.getQuantity());

        final Product newProduct = productService.save(convertToEntity(productDTORequest));

        return ResponseEntity
                .created(URI.create("/product/" + newProduct.getId()))
//                .created(new URI("/product/" + newProduct.getId()))
                .eTag(Integer.toString(newProduct.getVersion()))
                .body(convertToDTO(newProduct));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @RequestBody ProductDTO productUpdatedDTORequest,
            @PathVariable Long id,
            @RequestHeader("If-Match") Integer ifMatch
    ) {
        LOGGER.info("Updating product with id: {}, name: {}, quantity: {}", id, productUpdatedDTORequest.getName(), productUpdatedDTORequest.getQuantity());

        Optional<Product> product = productService.findById(id);

        return product.map(p -> {

            if (!p.getVersion().equals(ifMatch)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            p.setName(productUpdatedDTORequest.getName());
            p.setQuantity(productUpdatedDTORequest.getQuantity());
            p.setVersion(p.getVersion() + 1);

            if (productService.update(p)) {
                return ResponseEntity
                        .ok()
                        .location(URI.create("/product/" + p.getId()))
                        .eTag(Integer.toString(p.getVersion()))
                        .body(convertToDTO(p));
            } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        LOGGER.info("Deleting Product with id {}", id);

        Optional<Product> existingProduct = productService.findById(id);

        return existingProduct.map(p -> {

            if (productService.delete(p.getId())) {
                return ResponseEntity.ok().build();
            } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }).orElse(ResponseEntity.notFound().build());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        return product;
    }
}
