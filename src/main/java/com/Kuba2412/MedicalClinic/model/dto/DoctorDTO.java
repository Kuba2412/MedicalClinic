package com.Kuba2412.MedicalClinic.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<Long> institutionIds;
}
