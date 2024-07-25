package com.Kuba2412.MedicalClinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {

    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNumber;
    private List<Long> doctorIds;
}
