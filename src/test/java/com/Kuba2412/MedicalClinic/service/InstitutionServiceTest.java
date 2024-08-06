package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.handler.exception.InstitutionNotFound;
import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.InstitutionDTO;
import com.Kuba2412.MedicalClinic.model.mapper.InstitutionMapper;
import com.Kuba2412.MedicalClinic.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InstitutionServiceTest {

    private InstitutionService institutionService;
    private InstitutionRepository institutionRepository;
    private InstitutionMapper institutionMapper;

    @BeforeEach
    void setup() {
        institutionRepository = Mockito.mock(InstitutionRepository.class);
        institutionMapper = Mockito.mock(InstitutionMapper.class);
        institutionService = new InstitutionService(institutionRepository, institutionMapper);
    }

    @Test
    void createInstitution_ValidInstitutionDTO_InstitutionSaved() {
        // given
        InstitutionDTO institutionDTO = new InstitutionDTO();

        institutionDTO.setName("Klinika");
        institutionDTO.setCity("Warszawa");
        institutionDTO.setStreet("Górna");
        institutionDTO.setBuildingNumber("25");
        institutionDTO.setPostalCode("01-123");

        Institution institution = new Institution();
        institution.setName(institutionDTO.getName());
        institution.setCity(institutionDTO.getCity());
        institution.setStreet(institutionDTO.getStreet());
        institution.setBuildingNumber(institutionDTO.getBuildingNumber());
        institution.setPostalCode(institutionDTO.getPostalCode());
        when(institutionMapper.toInstitution(institutionDTO)).thenReturn(institution);

        // when
        institutionService.createInstitution(institutionDTO);

        // then
        verify(institutionMapper, times(1)).toInstitution(institutionDTO);
        verify(institutionRepository, times(1)).save(institution);
    }

    @Test
    void createInstitution_NullInstitutionDTO_ThrowsException() {
        //given
        InstitutionDTO institutionDTO = null;

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> institutionService.createInstitution(institutionDTO));
        assertEquals("Institution can't be null.", exception.getMessage());
        verify(institutionMapper, never()).toInstitution(any(InstitutionDTO.class));
        verify(institutionRepository, never()).save(any(Institution.class));
    }

    @Test
    void getAllInstitutions_NoInstitutionsExist_EmptyListReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Institution> institutionPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(institutionRepository.findAll(pageable)).thenReturn(institutionPage);

        // when
        List<InstitutionDTO> result = institutionService.getAllInstitutions(pageable);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(institutionMapper, never()).toInstitutionDTO(any(Institution.class));
    }

    @Test
    void getDoctorsForInstitution_InstitutionExists_DoctorsReturned() {
        // given
        Long institutionId = 1L;

        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setFirstName("Kuba");
        doctor1.setLastName("Ppp");
        doctor1.setSpecialization("Kardiologia");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setFirstName("Anna");
        doctor2.setLastName("Sss");
        doctor2.setSpecialization("Neurologia");

        Institution institution = new Institution();
        institution.setId(institutionId);
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);
        institution.setDoctors(doctors);

        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(institution));

        // when
        List<Doctor> result = institutionService.getDoctorsForInstitution(institutionId);

        // then
        assertNotNull(result);
        assertEquals(doctors.size(), result.size());

        assertEquals(doctor1.getId(), result.get(0).getId());
        assertEquals(doctor1.getFirstName(), result.get(0).getFirstName());
        assertEquals(doctor1.getLastName(), result.get(0).getLastName());
        assertEquals(doctor1.getSpecialization(), result.get(0).getSpecialization());

        assertEquals(doctor2.getId(), result.get(1).getId());
        assertEquals(doctor2.getFirstName(), result.get(1).getFirstName());
        assertEquals(doctor2.getLastName(), result.get(1).getLastName());
        assertEquals(doctor2.getSpecialization(), result.get(1).getSpecialization());
    }

    @Test
    void getDoctorsForInstitution_NonExsistentInstitution_IllegalArgumentExceptionThrown() {
        // given
        Long nonExsistenInstituionId = 12345L;

        // when + then
        Exception exception = assertThrows(InstitutionNotFound.class, () -> institutionService.getDoctorsForInstitution(nonExsistenInstituionId));
        assertEquals("Institution not found." , exception.getMessage());
        verify(institutionRepository, times(1)).findById(nonExsistenInstituionId);
    }
}