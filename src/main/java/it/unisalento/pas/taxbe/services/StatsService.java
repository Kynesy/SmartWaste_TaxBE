package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.repositories.IFeeRepository;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService implements IStatsService{

    private final IFeeRepository feeRepository;

    @Autowired
    public StatsService(IFeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    @Override
    public FeeStatistics getSumOfAllFeesByPayment(int year, int paidStatus) {
        List<Fee> feeList = feeRepository.findAll();
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != paidStatus);

        FeeStatistics feeStatistics = FeeUtils.sumFees(feeList);
        feeStatistics.setYear(year);
        return feeStatistics;
    }

    @Override
    public FeeStatistics getSumOfAllUserFeesByPayment(String userId, int year, int paidStatus) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != paidStatus);

        FeeStatistics feeStatistics = FeeUtils.sumFees(feeList);
        feeStatistics.setYear(year);
        return feeStatistics;
    }

    @Override
    public WasteStatistics getAllRegisteredWasteByUserID(String userId, int year) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        WasteStatistics paidWastes = FeeUtils.sumWastes(feeList);
        paidWastes.setUserId(userId);
        paidWastes.setYear(year);
        return paidWastes;
    }

}
