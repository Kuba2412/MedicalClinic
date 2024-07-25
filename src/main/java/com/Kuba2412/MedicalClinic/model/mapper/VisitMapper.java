package com.Kuba2412.MedicalClinic.model.mapper;

import com.Kuba2412.MedicalClinic.model.Visit;
import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface VisitMapper {

    VisitDTO visitToVisitDTO(Visit visit);

    Visit visitDTOToVisit(VisitDTO visitDTO);

}
