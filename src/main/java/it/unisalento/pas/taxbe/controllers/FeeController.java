package it.unisalento.pas.taxbe.controllers;


import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.dto.FeeDTO;
import it.unisalento.pas.taxbe.dto.FeeStatisticsDTO;
import it.unisalento.pas.taxbe.dto.WasteStatisticsDTO;
import it.unisalento.pas.taxbe.services.IFeeService;
import it.unisalento.pas.taxbe.services.IStatsService;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/fee")
public class FeeController {
    private final IFeeService feeService;
    private final IStatsService statsService;

    @Autowired
    public FeeController(IFeeService feeService, IStatsService statsService) {
        this.feeService = feeService;
        this.statsService = statsService;
    }

    @PostMapping("/create/all")
    public ResponseEntity<ArrayList<FeeDTO>> createFee(@RequestBody ArrayList<WasteStatisticsDTO> wasteStatListDTO){
        ArrayList<FeeDTO> createdFees = new ArrayList<>();

        for (WasteStatisticsDTO wasteStatDTO :
                wasteStatListDTO) {
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

    @DeleteMapping("/delete/{feeId}")
    public ResponseEntity<String> deleteFee(@PathVariable String feeId){
        if(feeService.deleteFee(feeId) == 0){
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Fee deleted successfully\"}");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error deleting fee\"}");
        }
    }

    @PostMapping("/pay/{feeId}")
    public ResponseEntity<String> payFee(@PathVariable String feeId){
        if(feeService.payFee(feeId) == 0){
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Fee paid\"}");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Fee not found\"}");
        }
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<ArrayList<FeeDTO>> getAllByUserId(@PathVariable String userId){
        ArrayList<Fee> feeList = feeService.getAllFeeByUserID(userId);
        ArrayList<FeeDTO> feeListDTO = new ArrayList<>();

        for (Fee fee :
                feeList) {
            FeeDTO feeDTO = fromFeeToFeeDTO(fee);
            feeListDTO.add(feeDTO);
        }

        return ResponseEntity.ok(feeListDTO);
    }

    @GetMapping("/get/all")
    public ResponseEntity<ArrayList<FeeDTO>> getAll(){
        ArrayList<Fee> feeList = feeService.getAllFees();
        ArrayList<FeeDTO> feeListDTO = new ArrayList<>();

        for (Fee fee :
                feeList) {
            FeeDTO feeDTO = fromFeeToFeeDTO(fee);
            feeListDTO.add(feeDTO);
        }

        return ResponseEntity.ok(feeListDTO);
    }

    @GetMapping("/statistics/paid/{year}/{paidStatus}")
    public ResponseEntity<FeeStatisticsDTO> getByPaidStatus(@PathVariable int year, @PathVariable int paidStatus){
        FeeStatistics statistics = statsService.getSumOfAllFeesByPayment(year, paidStatus);
        FeeStatisticsDTO statisticsDTO = fromFeeStatisticsToFeeStatisticsDTO(statistics);
        
        return ResponseEntity.ok(statisticsDTO);
    }

    @GetMapping("/statistics/user/paid/{userId}/{year}/{paidStatus}")
    public ResponseEntity<FeeStatisticsDTO> getByPaidStatus(@PathVariable String userId, @PathVariable int year, @PathVariable int paidStatus){
        FeeStatistics statistics = statsService.getSumOfAllUserFeesByPayment(userId, year, paidStatus);
        FeeStatisticsDTO statisticsDTO = fromFeeStatisticsToFeeStatisticsDTO(statistics);

        return ResponseEntity.ok(statisticsDTO);
    }

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

    private WasteStatistics fromStatisticsDTOtoStatistics(WasteStatisticsDTO statisticsDTO) {
        WasteStatistics statistics = new WasteStatistics();

        statistics.setUserId(statisticsDTO.getUserId());
        statistics.setYear(statisticsDTO.getYear());
        statistics.setTotalSortedWaste(statisticsDTO.getTotalSortedWaste());
        statistics.setTotalUnsortedWaste(statisticsDTO.getTotalUnsortedWaste());

        return statistics;
    }

}
