package aug.bueno.review.microservice.repository;

import aug.bueno.review.microservice.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Optional<Review> findByProductId(Long productId);
}
