package aug.bueno.review.microservice.unit.test.service;

import aug.bueno.review.microservice.domain.Review;
import aug.bueno.review.microservice.domain.ReviewEntry;
import aug.bueno.review.microservice.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    private ReviewService service;

    @MockBean
    private ReviewService repository;

    @Test
    void testFindByIdSuccess(){
        Review mockReview = new Review("reviewId", 1L, 1);

        Date now = new Date();

        mockReview.getEntries().add(new ReviewEntry("test-user", now, "Nice"));
        doReturn(Optional.of(mockReview)).when(repository).findById(mockReview.getId());

        Optional<Review> reviewReturned = service.findById(mockReview.getId());

        assertTrue(reviewReturned.isPresent());
        assertSame(reviewReturned.get(), mockReview);
    }

}
