package it.unisalento.pas.taxbe.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("fee")
@NoArgsConstructor
@Getter
@Setter
public class Fee {
    @Id private String id;
    private String timestamp;
    private String userId;
    private int paid;
    private int sortedWaste;
    private int unsortedWaste;
    private float sortedTax;
    private float unsortedTax;
}
