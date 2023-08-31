package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;

public interface IStatsService {
    FeeStatistics getSumOfAllFeesByPayment(int year, int paidStatus);

    WasteStatistics getAllRegisteredWasteByUserID(String userId, int year);

    FeeStatistics getSumOfAllUserFeesByPayment(String userId, int year, int paidStatus);
}
