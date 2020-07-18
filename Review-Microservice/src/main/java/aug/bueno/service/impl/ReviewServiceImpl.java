package aug.bueno.service.impl;

import aug.bueno.domain.Review;
import aug.bueno.repository.ReviewRepository;
import aug.bueno.service.ReviewService;

import java.util.List;
import java.util.Optional;

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
