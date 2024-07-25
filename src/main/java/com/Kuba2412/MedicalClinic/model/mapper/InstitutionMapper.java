package com.Kuba2412.MedicalClinic.model.mapper;

import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.InstitutionDTO;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    InstitutionDTO toInstitutionDTO(Institution institution);

    Institution toInstitution(InstitutionDTO institutionDTO);
}