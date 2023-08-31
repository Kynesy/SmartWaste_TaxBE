package it.unisalento.pas.taxbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FeeStatisticsDTO {
    int year;
    private int paid;
    private int totalSortedWaste;
    private int totalUnsortedWaste;
    private float totalSortedTax;
    private float totalUnsortedTax;
}
