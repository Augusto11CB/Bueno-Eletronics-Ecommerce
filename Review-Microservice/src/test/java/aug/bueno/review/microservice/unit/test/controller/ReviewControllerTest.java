package aug.bueno.review.microservice.unit.test.controller;

import aug.bueno.review.microservice.domain.Review;
import aug.bueno.review.microservice.domain.ReviewEntry;
import aug.bueno.review.microservice.service.ReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @MockBean
    private ReviewService service;

    @Autowired
    private MockMvc mockMvc;

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");


    @BeforeAll
    static void beforeAll() {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    void testGetReviewByIdFound() throws Exception {
        final Review mockReview = new Review("reviewId", 1L, 1);
        final Date now = new Date();
        final ReviewEntry reviewEntry = new ReviewEntry("test-user", now, "Nice");
        mockReview.getEntries().add(reviewEntry);

        doReturn(Optional.of(mockReview)).when(service).findById(mockReview.getId());

        mockMvc.perform(get("/review/{id}", mockReview.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/reviewId"))

                .andExpect(jsonPath("$.id", is("reviewId")))
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.entries[0].username", is("test-user")))
                //TODO Adjustments in the df to perform comparative
                //.andExpect(jsonPath("$.entries[0].date", is(df.format(now))))
                .andExpect(jsonPath("$.entries.length()", is(1)));
    }
}
