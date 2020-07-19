package aug.bueno.review.microservice.service;

import aug.bueno.review.microservice.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Optional<Review> findById(String id);

    List<Review> findAll();

    Optional<Review> findByProductId(Long productId);

    Review save(Review review);

    Review update(Review review);

    void delete(String id);
}
