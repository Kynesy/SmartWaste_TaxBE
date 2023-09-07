/**
 * Data Transfer Object (DTO) representing Fee Statistics information.
 * This DTO is used to transfer statistics related to fees between different parts of the application.
 */
package it.unisalento.pas.taxbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO class that encapsulates statistics related to fees for a specific year.
 */
@NoArgsConstructor
@Getter
@Setter
public class FeeStatisticsDTO {
    /**
     * The year for which the fee statistics are recorded.
     */
    int year;

    /**
     * The total amount paid for fees in the specified year.
     */
    private int paid;

    /**
     * The total amount of sorted waste for fees in the specified year.
     */
    private int totalSortedWaste;

    /**
     * The total amount of unsorted waste for fees in the specified year.
     */
    private int totalUnsortedWaste;

    /**
     * The total tax amount for sorted waste in the specified year.
     */
    private float totalSortedTax;

    /**
     * The total tax amount for unsorted waste in the specified year.
     */
    private float totalUnsortedTax;
}
