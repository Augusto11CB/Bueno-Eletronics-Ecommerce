package aug.bueno.review.microservice.service.impl;

import aug.bueno.review.microservice.domain.Review;
import aug.bueno.review.microservice.repository.ReviewRepository;
import aug.bueno.review.microservice.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> findById(String id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review save(Review review) {
        review.setVersion(1);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        review.setVersion(review.getVersion() + 1);
        return reviewRepository.save(review);
    }

    @Override
    public void delete(String id) {
        reviewRepository.deleteById(id);
    }
}
