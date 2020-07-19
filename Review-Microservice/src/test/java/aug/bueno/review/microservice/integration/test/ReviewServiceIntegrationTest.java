package aug.bueno.review.microservice.integration.test;

import aug.bueno.custom.MongoDataFile;
import aug.bueno.custom.junit.extension.MongoSpringCustomExtension;
import aug.bueno.review.microservice.domain.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MongoSpringCustomExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Test
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testGetReviewByIdFound() throws Exception {
        mockMvc.perform(get("/review/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/1"))

                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.entries[0].username", is("user1")))
                //TODO Adjustments in the df to perform comparative
                //.andExpect(jsonPath("$.entries[0].date", is(df.format(now))))
                .andExpect(jsonPath("$.entries.length()", is(1)));
    }
}
