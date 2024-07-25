package com.Kuba2412.MedicalClinic.model.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDoctorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;

}
