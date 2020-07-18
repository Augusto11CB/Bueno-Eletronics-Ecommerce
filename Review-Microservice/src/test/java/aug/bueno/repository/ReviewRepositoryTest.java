package aug.bueno.repository;

import aug.bueno.domain.Review;
import aug.bueno.domain.ReviewEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest // -> Loads an embedded MongoDB instance
public class ReviewRepositoryTest {

    private static File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample.json").toFile();

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void beforeEach() throws IOException {
        Review[] objects = mapper.readValue(SAMPLE_JSON, Review[].class);

        //Load each review into MongoDB
        Arrays.stream(objects).forEach(mongoTemplate::save);
    }

    @AfterEach
    void afterEach() {
        mongoTemplate.dropCollection("Reviews");
    }

    @Test
    void testFindAll() {
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(2, reviews.size());
    }

    @Test
    void testFindByIdOK() {
        Optional<Review> reviews = reviewRepository.findById("1");
        assertTrue(reviews.isPresent());
        reviews.ifPresent(r -> {
            assertEquals("1", r.getId());
            assertEquals(1L, r.getProductId());
            assertEquals(1, r.getVersion().intValue());
            assertEquals(1, r.getEntries().size());
        });
    }

    @Test
    void testFindByIdFailure() {
        Optional<Review> review = reviewRepository.findById("99");
        assertFalse(review.isPresent());
    }

    @Test
    void testFindByProductIdSuccess() {
        Optional<Review> review = reviewRepository.findByProductId(1L);
        assertTrue(review.isPresent());
    }

    @Test
    void testFindByProductIdFailure() {
        Optional<Review> review = reviewRepository.findByProductId(99L);
        assertFalse(review.isPresent());
    }

    @Test
    void testSave() {
        final Review review = new Review(10L, 1);
        final Date now = new Date();
        final ReviewEntry reviewEntry = new ReviewEntry("test-user", now, "Nice");
        review.getEntries().add(reviewEntry);

        final Review save = reviewRepository.save(review);

        Optional<Review> loadedReview = reviewRepository.findById(save.getId());


        // asserts
        assertTrue(loadedReview.isPresent());

        loadedReview.ifPresent(r -> {
            assertEquals(10L, r.getProductId());
            assertEquals(1, r.getVersion().intValue());
            assertEquals(1, r.getEntries().size());
        });
    }

    // TODO update

    @Test
    void deleteTest() {
        reviewRepository.deleteById("2");

        Optional<Review> review = reviewRepository.findById("2");

        assertFalse(review.isPresent());
    }
}
