package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.repositories.IFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeeService implements IFeeService{

    private final IFeeRepository feeRepository;

    @Autowired
    public FeeService(IFeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    @Override
    public int createFee(Fee feeToAdd) {
        feeRepository.save(feeToAdd);
        return 0;
    }

    @Override
    public int deleteFee(String feeId) {
        if(feeRepository.existsById(feeId)){
            feeRepository.deleteById(feeId);
            return 0;
        }
        return 1;
    }

    @Override
    public int payFee(String feeId) {

        Optional<Fee> optFee = feeRepository.findById(feeId);
        if (optFee.isPresent()){
            Fee feeToBePaid = optFee.get();
            feeToBePaid.setPaid(1);
            feeRepository.save(feeToBePaid);
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public ArrayList<Fee> getAllFeeByUserID(String userId) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        return new ArrayList<>(feeList);
    }

    @Override
    public ArrayList<Fee> getAllFees() {
        List<Fee> feeList = feeRepository.findAll();
        return new ArrayList<>(feeList);
    }

    @Override
    public WasteStatistics getNumberOfPaidWastesByUserID(String userId, int year) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != 1);

        WasteStatistics paidWastes = sumWastes(feeList);
        paidWastes.setUserId(userId);
        paidWastes.setYear(year);
        return paidWastes;
    }

    @Override
    public WasteStatistics getNumberOfWastesByPayment(int year, int paid) {
        List<Fee> feeList = feeRepository.findAll();
        feeList.removeIf(tmpFee -> !tmpFee.getTimestamp().startsWith(String.valueOf(year)) || tmpFee.getPaid() != paid);

        WasteStatistics paidWastes = sumWastes(feeList);
        paidWastes.setYear(year);
        return paidWastes;
    }

    private WasteStatistics sumWastes(List<Fee> feeList) {
        WasteStatistics statistics = new WasteStatistics();

        int sorted = 0;
        int unsorted = 0;
        for (Fee fee :
                feeList) {
            sorted = sorted + fee.getSortedWaste();
            unsorted = unsorted + fee.getUnsortedWaste();
        }

        return statistics;
    }



}
