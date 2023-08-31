package it.unisalento.pas.taxbe.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FeeStatistics {
    int year;
    private int paid;
    private int totalSortedWaste;
    private int totalUnsortedWaste;
    private float totalSortedTax;
    private float totalUnsortedTax;
}
