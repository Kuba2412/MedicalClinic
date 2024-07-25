package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.Patient;
import com.Kuba2412.MedicalClinic.model.dto.PatientDTO;
import com.Kuba2412.MedicalClinic.model.mapper.PatientMapper;
import com.Kuba2412.MedicalClinic.repository.PatientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    private PatientService patientService;
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;

    @BeforeEach
    void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientMapper = Mappers.getMapper(PatientMapper.class);
        patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void getPatientDtoByEmail_PatientExists_PatientDtoReturned() {
        // given
        String patientEmail = "patient@gmail.com";
        Patient patient = createPatient(patientEmail);
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        // when
        PatientDTO result = patientService.getPatientDtoByEmail(patientEmail);

        // then
        assertNotNull(result);
        assertEquals(patientEmail, result.getEmail());
    }

    @Test
    void getPatientDtoByEmail_NonExsistentEmail_IllegalArgumentExceptionThrown() {
        // given
        String nonExsistentEmail = "kp123@gmail.com";

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> patientService.getPatientDtoByEmail(nonExsistentEmail));
        assertEquals("Patient not found.", exception.getMessage());
        verify(patientRepository, times(1)).findByEmail(nonExsistentEmail);

    }

    @Test
    void getPatientDtosByFirstName_PatientsExist_PatientDtosReturned() {
        // given
        String firstName = "Kuba";
        Pageable pageable = PageRequest.of(0, 10);
        List<Patient> patients = Arrays.asList(createPatient("kuba1@gmail.com"), createPatient("kuba2@gmail.com"));
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());
        when(patientRepository.findByFirstName(firstName, pageable)).thenReturn(patientPage);

        // when
        List<PatientDTO> result = patientService.getPatientDtosByFirstName(firstName, pageable);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("kuba1@gmail.com", result.get(0).getEmail());
        assertEquals("kuba2@gmail.com", result.get(1).getEmail());
    }

    @Test
    void addPatient_ValidPatient_PatientSaved() {
        // given
        Patient patient = createPatient("newpatient@gmail.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // when
        Patient result = patientService.addPatient(patient);

        // then
        assertNotNull(result);
        assertEquals("newpatient@gmail.com", result.getEmail());
    }

    @Test
    void deletePatientByEmail_PatientExists_PatientDeleted() {
        // given
        String patientEmail = "patient@gmail.com";
        Patient patient = new Patient();
        patient.setEmail(patientEmail);
        when(patientRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patient));

        // when
        patientService.deletePatientByEmail(patientEmail);

        // then
        verify(patientRepository, times(1)).findByEmail(patientEmail);
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatientByEmail_PatientNotFound_ThrowsException() {
        // given
        String patientEmail2 = "nonexistentpatient@gmail.com";
        when(patientRepository.findByEmail(patientEmail2)).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> patientService.deletePatientByEmail(patientEmail2));
        assertEquals("Patient not found.", exception.getMessage());
        verify(patientRepository, times(1)).findByEmail(patientEmail2);
        verify(patientRepository, never()).delete(any(Patient.class));
    }

    @Test
    void updatePatientByEmail_PatientExists_PatientUpdated() {
        // given
        String patientEmail = "updatepatient@gmail.com";
        Patient patient = createPatient(patientEmail);
        PatientDTO newPatientDto = new PatientDTO();
        newPatientDto.setEmail(patientEmail);
        newPatientDto.setFirstName("UpdatedFirstName");
        newPatientDto.setLastName("UpdatedLastName");
        newPatientDto.setPhoneNumber("123456789");
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // when
        PatientDTO result = patientService.updatePatientByEmail(patientEmail, newPatientDto);

        // then
        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    void updatePatientByEmail_NonExsistentEmail_IllegalArgumentExceptionThrown() {
        // given
        String nonExsistentEmail1 = "kp12345@gmail.com";
        PatientDTO newPatientDto = new PatientDTO();

        newPatientDto.setEmail(nonExsistentEmail1);
        newPatientDto.setFirstName("UpdatedFirstName");
        newPatientDto.setLastName("UpdatedLastName");
        newPatientDto.setPhoneNumber("123456789");
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> patientService.updatePatientByEmail(nonExsistentEmail1, newPatientDto));
        assertEquals("Patient not found.", exception.getMessage());
        verify(patientRepository, times(1)).findByEmail(nonExsistentEmail1);
    }

    private Patient createPatient(String email) {
        return new Patient(1L, email, "password", "Kuba", "Pp", "1234567890", LocalDate.of(2001, 1, 1), null, null);
    }
}