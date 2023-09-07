/**
 * Service class responsible for handling statistical operations related to fees and waste.
 * This service provides methods for calculating statistics on fees and waste data.
 */
package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.repositories.IFeeRepository;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for calculating statistics on fees and waste in the application.
 */
@Service
public class StatsService implements IStatsService {

    private final IFeeRepository feeRepository;

    /**
     * Constructor for StatsService.
     *
     * @param feeRepository The repository for accessing fee-related data.
     */
    @Autowired
    public StatsService(IFeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    /**
     * Calculate the sum of all fees by payment status for a specific year.
     *
     * @param year       The year for which to calculate the statistics.
     * @param paidStatus The payment status (e.g., paid or unpaid).
     * @return A FeeStatistics object containing the sum of fees for the specified year and payment status.
     */
    @Override
    public FeeStatistics getSumOfAllFeesByPayment(int year, int paidStatus) {
        List<Fee> feeList = feeRepository.findAll();
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != paidStatus);

        FeeStatistics feeStatistics = FeeUtils.sumFees(feeList);
        feeStatistics.setYear(year);
        return feeStatistics;
    }

    /**
     * Calculate the sum of all user fees by payment status for a specific year.
     *
     * @param userId     The user ID for which to calculate the statistics.
     * @param year       The year for which to calculate the statistics.
     * @param paidStatus The payment status (e.g., paid or unpaid).
     * @return A FeeStatistics object containing the sum of user fees for the specified year and payment status.
     */
    @Override
    public FeeStatistics getSumOfAllUserFeesByPayment(String userId, int year, int paidStatus) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != paidStatus);

        FeeStatistics feeStatistics = FeeUtils.sumFees(feeList);
        feeStatistics.setYear(year);
        return feeStatistics;
    }

    /**
     * Calculate the sum of all registered waste by user ID for a specific year.
     *
     * @param userId The user ID for which to calculate the statistics.
     * @param year   The year for which to calculate the statistics.
     * @return A WasteStatistics object containing the sum of registered waste for the specified user and year.
     */
    @Override
    public WasteStatistics getAllRegisteredWasteByUserID(String userId, int year) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        WasteStatistics paidWastes = FeeUtils.sumWastes(feeList);
        paidWastes.setUserId(userId);
        paidWastes.setYear(year);
        return paidWastes;
    }
}
