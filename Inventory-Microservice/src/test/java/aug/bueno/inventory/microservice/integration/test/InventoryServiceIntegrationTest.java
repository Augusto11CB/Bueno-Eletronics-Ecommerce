package aug.bueno.inventory.microservice.integration.test;

import aug.bueno.inventory.microservice.model.InventoryRecord;
import aug.bueno.inventory.microservice.service.InventoryService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
public class InventoryServiceIntegrationTest {

    private WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryService service;


    @BeforeEach
    void beforeEach() {

        wireMockServer = new WireMockServer(9999);

        /**
         * When wireMock starts, it automatically looks in the mapping directory, finds the mapping files,
         * and configures the request and responses based on the found mapping files
         * */
        wireMockServer.start();
    }

    @Test
    void testGetInventoryRecordSuccess() throws Exception {

        mockMvc.perform(get("/inventory/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))


                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.quantity", is(500)))
                .andExpect(jsonPath("$.productName", is("Nice Name")))
                .andExpect(jsonPath("$.productCategory", is("Great Product")));
//
//        Optional<InventoryRecord> record = service.getInventoryRecord(1L);
//
//        assertTrue(record.isPresent());
//        assertEquals(500, record.get().getQuantity().intValue());

    }

}
