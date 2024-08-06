package com.Kuba2412.MedicalClinic.model.mapper;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.dto.DoctorDTO;

import com.Kuba2412.MedicalClinic.model.dto.SimpleDoctorDTO;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DoctorMapper {

    DoctorDTO toDoctorDTO(Doctor doctor);

    Doctor toDoctor(DoctorDTO doctorDTO);

    SimpleDoctorDTO toSimpleDoctorDTO(Doctor doctor);

}