package it.unisalento.pas.taxbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FeeDTO {
    private String id;
    private String timestamp;
    private String userId;
    private int paid;
    private int sortedWaste;
    private int unsortedWaste;
    private float sortedTax;
    private float unsortedTax;
}
