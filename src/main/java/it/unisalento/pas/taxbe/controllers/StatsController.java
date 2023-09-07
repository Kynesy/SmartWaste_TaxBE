package it.unisalento.pas.taxbe.controllers;

import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.dto.FeeStatisticsDTO;
import it.unisalento.pas.taxbe.services.IStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Questa classe gestisce le richieste relative alle statistiche delle tariffe.
 */
@RestController
@RequestMapping("/api/statistics")
public class StatsController {
    private final IStatsService statsService;

    /**
     * Costruttore della classe StatsController.
     *
     * @param statsService Servizio per le statistiche delle tariffe
     */
    public StatsController(IStatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Ottiene le statistiche per tutte le tariffe in base allo stato di pagamento e all'anno.
     *
     * @param year       Anno delle tariffe
     * @param paidStatus Stato di pagamento delle tariffe (1 per pagate, 0 per non pagate)
     * @return ResponseEntity contenente le statistiche delle tariffe in formato DTO
     */
    @GetMapping("/statistics/all/{year}/{paidStatus}")
    public ResponseEntity<FeeStatisticsDTO> getByPaidStatus(@PathVariable int year, @PathVariable int paidStatus) {
        FeeStatistics statistics = statsService.getSumOfAllFeesByPayment(year, paidStatus);
        FeeStatisticsDTO statisticsDTO = fromFeeStatisticsToFeeStatisticsDTO(statistics);

        return ResponseEntity.ok(statisticsDTO);
    }

    /**
     * Ottiene le statistiche per tutte le tariffe di un utente specifico in base allo stato di pagamento, all'anno e all'ID dell'utente.
     *
     * @param userId     ID dell'utente
     * @param year       Anno delle tariffe
     * @param paidStatus Stato di pagamento delle tariffe (1 per pagate, 0 per non pagate)
     * @return ResponseEntity contenente le statistiche delle tariffe dell'utente in formato DTO
     */
    @GetMapping("/statistics/user/{userId}/{year}/{paidStatus}")
    public ResponseEntity<FeeStatisticsDTO> getByPaidStatus(@PathVariable String userId, @PathVariable int year, @PathVariable int paidStatus) {
        FeeStatistics statistics = statsService.getSumOfAllUserFeesByPayment(userId, year, paidStatus);
        FeeStatisticsDTO statisticsDTO = fromFeeStatisticsToFeeStatisticsDTO(statistics);

        return ResponseEntity.ok(statisticsDTO);
    }

    /**
     * Converte un oggetto FeeStatistics in un oggetto FeeStatisticsDTO.
     *
     * @param statistics Statistiche delle tariffe
     * @return FeeStatisticsDTO contenente le statistiche delle tariffe in formato DTO
     */
    private FeeStatisticsDTO fromFeeStatisticsToFeeStatisticsDTO(FeeStatistics statistics) {
        FeeStatisticsDTO feeStatisticsDTO = new FeeStatisticsDTO();

        feeStatisticsDTO.setPaid(statistics.getPaid());
        feeStatisticsDTO.setTotalSortedTax(statistics.getTotalSortedTax());
        feeStatisticsDTO.setTotalUnsortedTax(statistics.getTotalUnsortedTax());
        feeStatisticsDTO.setTotalSortedWaste(statistics.getTotalSortedWaste());
        feeStatisticsDTO.setTotalUnsortedWaste(statistics.getTotalUnsortedWaste());
        feeStatisticsDTO.setYear(statistics.getYear());

        return feeStatisticsDTO;
    }
}
