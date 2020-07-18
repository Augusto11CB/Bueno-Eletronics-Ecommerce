package aug.bueno.controller;

import aug.bueno.domain.Review;
import aug.bueno.domain.ReviewEntry;
import aug.bueno.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@RestController
public class ReviewController {

    private static final Logger LOGGER = LogManager.getLogger(ReviewController.class);

    private ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }


    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReview(@PathVariable String id) {
        return service.findById(id).map(
                review -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(review.getVersion()))
                                .location(new URI("/review/" + review.getId()))
                                .body(review);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
        ).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/reviews")
    public Iterable<Review> getReview(@RequestParam(value = "productOd", required = false) Optional<String> productId) {

        return productId.map(pid -> {
                    return service.findByProductId(Long.valueOf(pid))
                            .map(Arrays::asList)
                            .orElseGet(ArrayList::new);
                }

        ).orElse(service.findAll());
    }


    @PostMapping("/review")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        LOGGER.info("Creating new Review for product id: {}, {}", review.getId(), review);

        review.getEntries().forEach(reviewEntry -> reviewEntry.setDate(new Date()));

        final Review newReview = service.save(review);

        try {
            return ResponseEntity.created(new URI("/review/" + newReview.getId()))
                    .eTag(Integer.toString(newReview.getVersion()))
                    .body(newReview);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/review/{productID}/entry")
    public ResponseEntity<Review> addEntryReview(@PathVariable Long productID, @RequestBody ReviewEntry newEntry) {
        LOGGER.info("Add review entry for for product id: {}, {}", productID, newEntry);

        Review review = service.findByProductId(productID).orElseGet(() -> new Review(productID));

        newEntry.setDate(new Date());
        review.getEntries().add(newEntry);

        final Review updatedReview = service.update(review);

        LOGGER.info("Updated review: {}", updatedReview);

        try {
            return ResponseEntity
                    .ok()
                    .eTag(Integer.toString(updatedReview.getVersion()))
                    .location(new URI("/review/" + review.getId()))
                    .body(updatedReview);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        LOGGER.info("Deleting review with ID: {}", id);

        Optional<Review> existingReview = service.findById(id);

        return existingReview.map(review -> {
            service.delete(review.getId());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
