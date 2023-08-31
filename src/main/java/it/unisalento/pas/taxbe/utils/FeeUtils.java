package it.unisalento.pas.taxbe.utils;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;

import java.time.LocalDate;
import java.util.List;

public class FeeUtils {
    private final static float SORTED_KG_VALUE = 2;
    private final static float UNSORTED_KG_VALUE = 5;

    public static Fee calculateFee(WasteStatistics newStat, WasteStatistics oldStat){
        int[] diff = getDiff(newStat, oldStat);

        if(diff[0] > 0 || diff[1] > 0){
            return buildFee(newStat.getUserId(), diff[0], diff[1]);
        }else{
            return null;
        }
    }

    private static int[] getDiff(WasteStatistics newStat, WasteStatistics oldStat){
        int[] diff = new int[2];
        diff[0] = newStat.getTotalSortedWaste() - oldStat.getTotalSortedWaste();
        diff[1] = newStat.getTotalUnsortedWaste() - oldStat.getTotalUnsortedWaste();
        return diff;
    }

    private static Fee buildFee(String userId, int sorted, int unsorted){
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

    public static WasteStatistics sumWastes(List<Fee> feeList) {
        WasteStatistics statistics = new WasteStatistics();

        statistics.setTotalUnsortedWaste(0);
        statistics.setTotalSortedWaste(0);

        for (Fee fee :
                feeList) {
            statistics.setTotalSortedWaste(statistics.getTotalSortedWaste() + fee.getSortedWaste());
            statistics.setTotalUnsortedWaste(statistics.getTotalUnsortedWaste() + fee.getUnsortedWaste());
        }

        return statistics;
    }

    public static FeeStatistics sumFees(List<Fee> feeList) {
        FeeStatistics feeStatistics = new FeeStatistics();

        feeStatistics.setTotalSortedWaste(0);
        feeStatistics.setTotalUnsortedWaste(0);
        feeStatistics.setTotalSortedTax(0);
        feeStatistics.setTotalUnsortedTax(0);

        for (Fee fee :
                feeList) {
            feeStatistics.setTotalUnsortedTax(feeStatistics.getTotalUnsortedTax() + fee.getUnsortedTax());
            feeStatistics.setTotalUnsortedWaste(feeStatistics.getTotalUnsortedWaste() + fee.getUnsortedWaste());
            feeStatistics.setTotalSortedTax(feeStatistics.getTotalSortedTax() + fee.getSortedTax());
            feeStatistics.setTotalSortedWaste(feeStatistics.getTotalSortedWaste() + fee.getSortedWaste() );
        }

        return feeStatistics;
    }
}
