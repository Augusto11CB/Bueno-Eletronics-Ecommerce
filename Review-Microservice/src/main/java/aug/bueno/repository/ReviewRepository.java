package aug.bueno.repository;

import aug.bueno.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Optional<Review> findByProductId(Long productId);
}
