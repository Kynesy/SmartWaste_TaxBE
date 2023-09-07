/**
 * Represents statistics related to fees in the application's domain.
 */
package it.unisalento.pas.taxbe.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class for storing statistics related to fees, categorized by year.
 */
@NoArgsConstructor
@Getter
@Setter
public class FeeStatistics {
    /**
     * The year for which the statistics are recorded.
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
