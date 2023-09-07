package it.unisalento.pas.taxbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO class that encapsulates information about a fee.
 */
@NoArgsConstructor
@Getter
@Setter
public class FeeDTO {
    /**
     * The unique identifier for a fee.
     */
    private String id;

    /**
     * The timestamp associated with this fee.
     */
    private String timestamp;

    /**
     * The user ID related to this fee.
     */
    private String userId;

    /**
     * The amount paid for this fee.
     */
    private int paid;

    /**
     * The amount of sorted waste associated with this fee.
     */
    private int sortedWaste;

    /**
     * The amount of unsorted waste associated with this fee.
     */
    private int unsortedWaste;

    /**
     * The tax amount for sorted waste in this fee.
     */
    private float sortedTax;

    /**
     * The tax amount for unsorted waste in this fee.
     */
    private float unsortedTax;
}
