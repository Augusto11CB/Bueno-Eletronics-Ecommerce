package aug.bueno.review.microservice.unit.test.repository;

import aug.bueno.custom.MongoDataFile;
import aug.bueno.custom.junit.extension.MongoSpringCustomExtension;
import aug.bueno.review.microservice.domain.Review;
import aug.bueno.review.microservice.domain.ReviewEntry;
import aug.bueno.review.microservice.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest // -> Loads an embedded MongoDB instance
@ExtendWith(MongoSpringCustomExtension.class)
public class ReviewRepositoryTest {

    // v1
//    private static File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample.json").toFile();
    // v1
//    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    // v1
//    @BeforeEach
//    void beforeEach() throws IOException {
//        Review[] objects = mapper.readValue(SAMPLE_JSON, Review[].class);
//
//        //Load each review into MongoDB
//        Arrays.stream(objects).forEach(mongoTemplate::save);
//    }

    // v1
//    @AfterEach
//    void afterEach() {
//        mongoTemplate.dropCollection("Reviews");
//    }


    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testFindAll() {
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(2, reviews.size());
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
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
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testFindByIdFailure() {
        Optional<Review> review = reviewRepository.findById("99");
        assertFalse(review.isPresent());
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testFindByProductIdSuccess() {
        Optional<Review> review = reviewRepository.findByProductId(1L);
        assertTrue(review.isPresent());
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testFindByProductIdFailure() {
        Optional<Review> review = reviewRepository.findByProductId(99L);
        assertFalse(review.isPresent());
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
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
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void deleteTest() {
        reviewRepository.deleteById("2");

        Optional<Review> review = reviewRepository.findById("2");

        assertFalse(review.isPresent());
    }
}
