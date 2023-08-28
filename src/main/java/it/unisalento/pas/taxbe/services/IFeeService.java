package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;

import java.util.ArrayList;

public interface IFeeService {
    int createFee(Fee feeToAdd);
    int deleteFee(String feeId);
    int payFee(String feeId);
    ArrayList<Fee> getAllFeeByUserID(String userId);
    ArrayList<Fee> getAllFees();
    WasteStatistics getNumberOfPaidWastesByUserID(String userId, int year);
    WasteStatistics getNumberOfWastesByPayment(int year, int paid);
}
