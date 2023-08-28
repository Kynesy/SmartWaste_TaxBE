package it.unisalento.pas.taxbe.controllers;


import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.dto.FeeDTO;
import it.unisalento.pas.taxbe.dto.WasteStatisticsDTO;
import it.unisalento.pas.taxbe.services.IFeeService;
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

    @Autowired
    public FeeController(IFeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createFee(@RequestBody WasteStatisticsDTO statisticsDTO){
        WasteStatistics newStat = fromStatisticsDTOtoStatistics(statisticsDTO);
        WasteStatistics oldStat = feeService.getAllRegistredWasteByUserID(newStat.getUserId(), newStat.getYear());

        Fee feeToCreate = FeeUtils.calculateFee(newStat, oldStat);
        if(feeToCreate != null){
            feeService.createFee(feeToCreate);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Fee created successfully\"}");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"No new wastes to fee\"}");
        }
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

    @GetMapping("/get/paid/{year}/{paidStatus}")
    public ResponseEntity<WasteStatisticsDTO> getByPaidStatus(@PathVariable int year, @PathVariable int paidStatus){
        WasteStatistics statistics = feeService.getNumberOfWastesByPayment(year, paidStatus);
        WasteStatisticsDTO statisticsDTO = fromStatisticsToStatisticsDTO(statistics);
        
        return ResponseEntity.ok(statisticsDTO);
    }

    private WasteStatisticsDTO fromStatisticsToStatisticsDTO(WasteStatistics statistics) {
        WasteStatisticsDTO statisticsDTO = new WasteStatisticsDTO();

        statisticsDTO.setUserId(statistics.getUserId());
        statisticsDTO.setYear(statistics.getYear());
        statisticsDTO.setTotalSortedWaste(statistics.getTotalSortedWaste());
        statisticsDTO.setTotalUnsortedWaste(statistics.getTotalUnsortedWaste());

        return statisticsDTO;
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
