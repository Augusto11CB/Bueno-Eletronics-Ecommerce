package aug.bueno.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Reviews")
public class Review {

    private String id;

    private Long productId;

    private Integer version = 1;

    private List<ReviewEntry> entries = new ArrayList<>();

    public Review(Long productId) {
        this.productId = productId;
    }

    public Review(String reviewID, Long productID, Integer version) {
        this.productId = productID;
        this.id = reviewID;
        this.version = version;

    }
}
