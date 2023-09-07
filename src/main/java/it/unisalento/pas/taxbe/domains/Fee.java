/**
 * Represents a Fee entity in the application's domain.
 */
package it.unisalento.pas.taxbe.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A document in the "fee" collection in the MongoDB database.
 */
@Document("fee")
@NoArgsConstructor
@Getter
@Setter
public class Fee {
    /**
     * The unique identifier for a Fee document.
     */
    @Id private String id;

    /**
     * The timestamp associated with this Fee.
     */
    private String timestamp;

    /**
     * The user ID related to this Fee.
     */
    private String userId;

    /**
     * The amount paid for this Fee.
     */
    private int paid;

    /**
     * The amount of sorted waste for this Fee.
     */
    private int sortedWaste;

    /**
     * The amount of unsorted waste for this Fee.
     */
    private int unsortedWaste;

    /**
     * The tax amount for sorted waste in this Fee.
     */
    private float sortedTax;

    /**
     * The tax amount for unsorted waste in this Fee.
     */
    private float unsortedTax;
}
