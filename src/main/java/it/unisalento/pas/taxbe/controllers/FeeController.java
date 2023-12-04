package it.unisalento.pas.taxbe.controllers;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.dto.FeeDTO;
import it.unisalento.pas.taxbe.dto.WasteStatisticsDTO;
import it.unisalento.pas.taxbe.services.IFeeService;
import it.unisalento.pas.taxbe.services.IStatsService;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Questa classe gestisce le richieste relative alle tariffe e alle statistiche delle tariffe.
 */
@RestController
@RequestMapping("/api/fee")
public class FeeController {
    private final IFeeService feeService;
    private final IStatsService statsService;

    /**
     * Costruttore della classe FeeController.
     *
     * @param feeService   Servizio per le tariffe
     * @param statsService Servizio per le statistiche delle tariffe
     */
    @Autowired
    public FeeController(IFeeService feeService, IStatsService statsService) {
        this.feeService = feeService;
        this.statsService = statsService;
    }

    /**
     * Crea tariffe basate sulle statistiche dei rifiuti fornite.
     *
     * @param wasteStatListDTO Lista di statistiche dei rifiuti in formato DTO
     * @return ResponseEntity contenente le tariffe create in formato DTO
     */
    @PostMapping("/create/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArrayList<FeeDTO>> createFee(@RequestBody ArrayList<WasteStatisticsDTO> wasteStatListDTO) {
        ArrayList<FeeDTO> createdFees = new ArrayList<>();

        for (WasteStatisticsDTO wasteStatDTO : wasteStatListDTO) {
            WasteStatistics newStat = fromStatisticsDTOtoStatistics(wasteStatDTO);
            WasteStatistics oldStat = statsService.getAllRegisteredWasteByUserID(newStat.getUserId(), newStat.getYear());

            Fee feeToCreate = FeeUtils.calculateFee(newStat, oldStat);
            if (feeToCreate != null) {
                feeService.createFee(feeToCreate);
                FeeDTO feeDTO = fromFeeToFeeDTO(feeToCreate);
                createdFees.add(feeDTO);
            }
        }

        return ResponseEntity.ok(createdFees);
    }

    /**
     * Elimina una tariffa in base all'ID.
     *
     * @param feeId ID della tariffa da eliminare
     * @return ResponseEntity con un messaggio di conferma o errore
     */
    @DeleteMapping("/delete/{feeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteFee(@PathVariable String feeId) {
        if (feeService.deleteFee(feeId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Tariffa eliminata con successo\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Errore nell'eliminazione della tariffa\"}");
        }
    }

    /**
     * Registra il pagamento di una tariffa in base all'ID.
     *
     * @param feeId ID della tariffa da pagare
     * @return ResponseEntity con un messaggio di conferma o errore
     */
    @PostMapping("/pay/{feeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> payFee(@PathVariable String feeId) {
        if (feeService.payFee(feeId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Tariffa pagata\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Tariffa non trovata\"}");
        }
    }

    /**
     * Ottiene tutte le tariffe di un utente in base all'ID dell'utente.
     *
     * @param userId ID dell'utente
     * @return ResponseEntity contenente le tariffe dell'utente in formato DTO
     */
    @GetMapping("/get/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ArrayList<FeeDTO>> getAllByUserId(@PathVariable String userId) {
        ArrayList<Fee> feeList = feeService.getAllFeeByUserID(userId);
        ArrayList<FeeDTO> feeListDTO = new ArrayList<>();

        for (Fee fee : feeList) {
            FeeDTO feeDTO = fromFeeToFeeDTO(fee);
            feeListDTO.add(feeDTO);
        }

        return ResponseEntity.ok(feeListDTO);
    }

    /**
     * Ottiene tutte le tariffe.
     *
     * @return ResponseEntity contenente tutte le tariffe in formato DTO
     */
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArrayList<FeeDTO>> getAll() {
        ArrayList<Fee> feeList = feeService.getAllFees();
        ArrayList<FeeDTO> feeListDTO = new ArrayList<>();

        for (Fee fee : feeList) {
            FeeDTO feeDTO = fromFeeToFeeDTO(fee);
            feeListDTO.add(feeDTO);
        }

        return ResponseEntity.ok(feeListDTO);
    }

    /**
     * Converte un oggetto Fee in un oggetto FeeDTO.
     *
     * @param fee Tariffa
     * @return FeeDTO contenente la tariffa in formato DTO
     */
    private FeeDTO fromFeeToFeeDTO(Fee fee) {
        FeeDTO feeDTO = new FeeDTO();

        feeDTO.setId(fee.getId());
        feeDTO.setUserId(fee.getUserId());
        feeDTO.setTimestamp(fee.getTimestamp());
        feeDTO.setPaid(fee.getPaid());
        feeDTO.setSortedWaste(fee.getSortedWaste());
        feeDTO.setSortedTax(fee.getSortedTax());
        feeDTO.setUnsortedWaste(fee.getUnsortedWaste());
        feeDTO.setUnsortedTax(fee.getUnsortedTax());

        return feeDTO;
    }

    /**
     * Converte un oggetto WasteStatisticsDTO in un oggetto WasteStatistics.
     *
     * @param statisticsDTO Statistiche dei rifiuti in formato DTO
     * @return WasteStatistics contenente le statistiche dei rifiuti
     */
    private WasteStatistics fromStatisticsDTOtoStatistics(WasteStatisticsDTO statisticsDTO) {
        WasteStatistics statistics = new WasteStatistics();

        statistics.setUserId(statisticsDTO.getUserId());
        statistics.setYear(statisticsDTO.getYear());
        statistics.setTotalSortedWaste(statisticsDTO.getTotalSortedWaste());
        statistics.setTotalUnsortedWaste(statisticsDTO.getTotalUnsortedWaste());

        return statistics;
    }
}
