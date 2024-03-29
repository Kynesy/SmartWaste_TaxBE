/**
 * Represents statistics related to waste generated by users in the application's domain.
 */
package it.unisalento.pas.taxbe.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class for storing statistics related to waste generation by users, categorized by user and year.
 */
@NoArgsConstructor
@Getter
@Setter
public class WasteStatistics {
    /**
     * The user ID associated with the waste statistics.
     */
    String userId;

    /**
     * The year for which the waste statistics are recorded.
     */
    int year;

    /**
     * The total amount of sorted waste generated by the user in the specified year.
     */
    int totalSortedWaste;

    /**
     * The total amount of unsorted waste generated by the user in the specified year.
     */
    int totalUnsortedWaste;
}
