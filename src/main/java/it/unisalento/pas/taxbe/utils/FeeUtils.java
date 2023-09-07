/**
 * Utility class containing methods for fee calculations and statistics.
 * This utility class provides methods for calculating fees, summarizing waste statistics, and summarizing fee statistics.
 */
package it.unisalento.pas.taxbe.utils;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class for fee calculations and statistics.
 */
public class FeeUtils {
    private final static float SORTED_KG_VALUE = 2;
    private final static float UNSORTED_KG_VALUE = 5;

    /**
     * Calculates a fee based on the difference between new and old waste statistics.
     *
     * @param newStat The new waste statistics.
     * @param oldStat The old waste statistics.
     * @return A Fee object representing the calculated fee, or null if no fee is generated.
     */
    public static Fee calculateFee(WasteStatistics newStat, WasteStatistics oldStat) {
        int[] diff = getDiff(newStat, oldStat);

        if (diff[0] > 0 || diff[1] > 0) {
            return buildFee(newStat.getUserId(), diff[0], diff[1]);
        } else {
            return null;
        }
    }

    /**
     * Calculates the difference between new and old waste statistics.
     *
     * @param newStat The new waste statistics.
     * @param oldStat The old waste statistics.
     * @return An array containing the differences in sorted and unsorted waste quantities.
     */
    private static int[] getDiff(WasteStatistics newStat, WasteStatistics oldStat) {
        int[] diff = new int[2];
        diff[0] = newStat.getTotalSortedWaste() - oldStat.getTotalSortedWaste();
        diff[1] = newStat.getTotalUnsortedWaste() - oldStat.getTotalUnsortedWaste();
        return diff;
    }

    /**
     * Builds a Fee object based on waste quantities.
     *
     * @param userId  The user ID associated with the fee.
     * @param sorted  The quantity of sorted waste.
     * @param unsorted The quantity of unsorted waste.
     * @return A Fee object representing the calculated fee.
     */
    private static Fee buildFee(String userId, int sorted, int unsorted) {
        Fee fee = new Fee();

        fee.setTimestamp(String.valueOf(LocalDate.now()));
        fee.setUserId(userId);
        fee.setSortedWaste(sorted);
        fee.setUnsortedWaste(unsorted);
        fee.setSortedTax(sorted * SORTED_KG_VALUE);
        fee.setUnsortedTax(unsorted * UNSORTED_KG_VALUE);
        fee.setPaid(0);

        return fee;
    }

    /**
     * Summarizes waste statistics from a list of fees.
     *
     * @param feeList The list of fees to summarize.
     * @return A WasteStatistics object containing the total sorted and unsorted waste quantities.
     */
    public static WasteStatistics sumWastes(List<Fee> feeList) {
        WasteStatistics statistics = new WasteStatistics();

        statistics.setTotalUnsortedWaste(0);
        statistics.setTotalSortedWaste(0);

        for (Fee fee : feeList) {
            statistics.setTotalSortedWaste(statistics.getTotalSortedWaste() + fee.getSortedWaste());
            statistics.setTotalUnsortedWaste(statistics.getTotalUnsortedWaste() + fee.getUnsortedWaste());
        }

        return statistics;
    }

    /**
     * Summarizes fee statistics from a list of fees.
     *
     * @param feeList The list of fees to summarize.
     * @return A FeeStatistics object containing the total sorted and unsorted fees and taxes.
     */
    public static FeeStatistics sumFees(List<Fee> feeList) {
        FeeStatistics feeStatistics = new FeeStatistics();

        feeStatistics.setTotalSortedWaste(0);
        feeStatistics.setTotalUnsortedWaste(0);
        feeStatistics.setTotalSortedTax(0);
        feeStatistics.setTotalUnsortedTax(0);

        for (Fee fee : feeList) {
            feeStatistics.setTotalUnsortedTax(feeStatistics.getTotalUnsortedTax() + fee.getUnsortedTax());
            feeStatistics.setTotalUnsortedWaste(feeStatistics.getTotalUnsortedWaste() + fee.getUnsortedWaste());
            feeStatistics.setTotalSortedTax(feeStatistics.getTotalSortedTax() + fee.getSortedTax());
            feeStatistics.setTotalSortedWaste(feeStatistics.getTotalSortedWaste() + fee.getSortedWaste());
        }

        return feeStatistics;
    }
}
