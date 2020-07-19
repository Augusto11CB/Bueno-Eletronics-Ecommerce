package aug.bueno.service;

import aug.bueno.model.InventoryRecord;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class InventoryServiceTest {

    @Autowired
    private InventoryService service;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {

        wireMockServer = new WireMockServer(9999);

        /**
         * When wireMock starts, it automatically looks in the mapping directory, finds the mapping files,
         * and configures the request and responses based on the found mapping files
         * */
        wireMockServer.start();



        // configure the requests - Not externalized approach

//        wireMockServer.stubFor(get(urlEqualTo("/inventory/1"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(HttpStatus.OK.value())
//                        .withBodyFile("json/inventory-response.json")
//                )
//        );
//
//        wireMockServer.stubFor(get(urlEqualTo("/inventory/2"))
//                .willReturn(aResponse()
//                        .withStatus(HttpStatus.NOT_FOUND.value())
//                )
//        );
//
//        wireMockServer.stubFor(post(urlEqualTo("/inventory/1/purchaseRecord"))
//                .withHeader("Content-Type", containing("application/json"))
//
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(HttpStatus.OK.value())
//                        .withBodyFile("json/inventory-response-after-post.json")
//                )
//        );

    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    void testGetInventoryRecordSuccess() {

        Optional<InventoryRecord> record = service.getInventoryRecord(1L);

        assertTrue(record.isPresent());
        assertEquals(500, record.get().getQuantity().intValue());

    }

    @Test
    void testGetInventoryRecordNotFound() {

        Optional<InventoryRecord> record = service.getInventoryRecord(2L);

        assertFalse(record.isPresent());
    }

    @Test
    void testPurchaseProductSuccess() {

        Optional<InventoryRecord> record = service.purchaseProduct(1L, 1);

        assertTrue(record.isPresent());
        assertEquals(499, record.get().getQuantity().intValue());

    }

}
