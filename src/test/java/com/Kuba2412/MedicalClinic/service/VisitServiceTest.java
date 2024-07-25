package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.Patient;
import com.Kuba2412.MedicalClinic.model.Visit;
import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import com.Kuba2412.MedicalClinic.model.mapper.VisitMapper;
import com.Kuba2412.MedicalClinic.repository.DoctorRepository;
import com.Kuba2412.MedicalClinic.repository.PatientRepository;
import com.Kuba2412.MedicalClinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VisitServiceTest {

    private VisitService visitService;
    private VisitRepository visitRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private VisitMapper visitMapper;

    @BeforeEach
    void setup() {
        visitRepository = Mockito.mock(VisitRepository.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        visitMapper = Mockito.mock(VisitMapper.class);
        visitService = new VisitService(visitRepository, patientRepository, visitMapper, doctorRepository);
    }

    @Test
    void createVisit_ValidInput_VisitCreated() {
        // given
        LocalDateTime startVisit = LocalDateTime.now().plusDays(1);
        LocalDateTime endVisit = startVisit.plusHours(1);

        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setStartVisit(startVisit);
        visitDTO.setEndVisit(endVisit);

        Visit visit = new Visit();
        visit.setStartVisit(startVisit);
        visit.setEndVisit(endVisit);

        when(visitMapper.visitDTOToVisit(visitDTO)).thenReturn(visit);
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        // when
        visitService.createVisit(visitDTO);

        // then
        verify(visitMapper, times(1)).visitDTOToVisit(visitDTO);
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void createVisit_InvalidStartDate_ThrowsException() {
        // given
        LocalDateTime invalidStartVisit = LocalDateTime.now().minusDays(1);
        LocalDateTime endVisit = LocalDateTime.now();

        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setStartVisit(invalidStartVisit);
        visitDTO.setEndVisit(endVisit);

        Visit visit = new Visit();
        visit.setStartVisit(invalidStartVisit);
        visit.setEndVisit(endVisit);

        when(visitMapper.visitDTOToVisit(visitDTO)).thenReturn(visit);

        // when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitDTO));
        assertEquals("Invalid visit start date.", exception.getMessage());

        verify(visitMapper, times(1)).visitDTOToVisit(visitDTO);
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void getAllVisitsForPatient_VisitsExist_VisitsReturned() {
        // given
        Long patientId = 1L;

        LocalDateTime startDate1 = LocalDateTime.of(2025, 1, 1, 9, 0);
        LocalDateTime endDate1 = startDate1.plusHours(1);

        LocalDateTime startDate2 = LocalDateTime.of(2025, 1, 1, 14, 0);
        LocalDateTime endDate2 = startDate2.plusHours(2);

        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setStartVisit(startDate1);
        visit1.setEndVisit(endDate1);

        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setStartVisit(startDate2);
        visit2.setEndVisit(endDate2);

        List<Visit> visits = Arrays.asList(visit1, visit2);
        when(visitRepository.findAllByPatientId(patientId)).thenReturn(visits);
        when(visitMapper.visitToVisitDTO(visit1)).thenReturn(new VisitDTO());
        when(visitMapper.visitToVisitDTO(visit2)).thenReturn(new VisitDTO());

        // when
        List<VisitDTO> result = visitService.getAllVisitsForPatient(patientId);

        // then
        assertNotNull(result);
        assertEquals(visits.size(), result.size());
        assertEquals(visit1.getId(), result.get(0).getId());
        assertEquals(visit2.getId(), result.get(1).getId());
    }

    @Test
    void getAllVisitsForPatient_NoVisitsExist_EmptyListReturned() {
        // given
        Long patientId = 1L;
        List<Visit> emptyVisits = Collections.emptyList();
        when(visitRepository.findAllByPatientId(patientId)).thenReturn(emptyVisits);
        when(visitMapper.visitToVisitDTO(any(Visit.class))).thenReturn(new VisitDTO());

        // when
        List<VisitDTO> result = visitService.getAllVisitsForPatient(patientId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(visitRepository, times(1)).findAllByPatientId(patientId);
        verify(visitMapper, never()).visitToVisitDTO(any(Visit.class));
    }

    @Test
    void registerPatientForVisit_ValidVisitAndPatient_PatientRegistered() {
        // given
        Long visitId = 1L;
        Long patientId = 1L;
        LocalDateTime futureVisitDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        Visit visit = new Visit();
        visit.setStartVisit(futureVisitDate);
        Patient patient = new Patient();
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // when
        visitService.registerPatientForVisit(visitId, patientId);

        // then
        assertEquals(patient, visit.getPatient());
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void registerPatientForVisit_NonExistentVisit_ThrowsException() {
        // given
        Long visitId = 12345L;
        Long patientId = 1L;
        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitService.registerPatientForVisit(visitId, patientId));
        assertEquals("Visit not found", exception.getMessage());
        verify(visitRepository, times(1)).findById(visitId);
        verify(patientRepository, never()).findById(anyLong());
    }

    @Test
    void registerPatientForVisit_NonExistentPatient_ThrowsException() {
        // given
        Long visitId = 1L;
        Long patientId = 12345L;
        LocalDateTime futureVisitDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        Visit visit = new Visit();
        visit.setStartVisit(futureVisitDate);
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitService.registerPatientForVisit(visitId, patientId));
        assertEquals("Patient not found", exception.getMessage());
        verify(visitRepository, times(1)).findById(visitId);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void getAvailableVisitsForSpecializationAndDate_VisitsExist_VisitsReturned() {
        // given
        String specialization = "Cardiology";
        LocalDate date = LocalDate.of(2024, 10, 15);

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setStartVisit(date.atStartOfDay().plusHours(9));
        visit.setEndVisit(date.atStartOfDay().plusHours(10));

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setSpecialization(specialization);
        doctor.setVisits(List.of(visit));

        when(doctorRepository.findBySpecialization(specialization)).thenReturn(List.of(doctor));
        when(visitMapper.visitToVisitDTO(visit)).thenReturn(new VisitDTO());

        List<VisitDTO> result = visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(visit.getId(), result.get(0).getId());
    }

    @Test
    void getAvailableVisitsByDoctorSpecializationAndByDate_NoVisitsAvailable_EmptyListReturned() {
        // given
        String specialization = "Cardiology";
        LocalDate date = LocalDate.of(2024, 10, 15);

        when(doctorRepository.findBySpecialization(specialization)).thenReturn(Collections.emptyList());

        // when
        List<VisitDTO> result = visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getVisitsByDoctorId_DoctorExists_VisitsReturned() {
        // given
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setStartVisit(LocalDateTime.now().plusDays(1));
        visit.setEndVisit(LocalDateTime.now().plusDays(1).plusHours(1));

        doctor.setVisits(List.of(visit));

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(visitMapper.visitToVisitDTO(visit)).thenReturn(new VisitDTO());

        // when
        List<VisitDTO> result = visitService.getVisitsByDoctorId(doctorId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(visit.getId(), result.get(0).getId());
    }

    @Test
    void getVisitsByDoctorId_DoctorNotFound_ThrowsException() {
        // given
        Long doctorId = 9999L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitService.getVisitsByDoctorId(doctorId));
        assertEquals("Doctor not found", exception.getMessage());
    }

    @Test
    void getVisitsByPatientEmail_PatientExists_VisitsReturned() {
        // given
        String email = "patient@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setStartVisit(LocalDateTime.now().plusDays(1));
        visit.setEndVisit(LocalDateTime.now().plusDays(1).plusHours(1));

        patient.setVisits(List.of(visit));

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(visitMapper.visitToVisitDTO(visit)).thenReturn(new VisitDTO());

        // when
        List<VisitDTO> result = visitService.getVisitsByPatientEmail(email);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(visit.getId(), result.get(0).getId());
    }

    @Test
    void getVisitsByPatientEmail_PatientNotFound_ThrowsException() {
        // given
        String email = "nonexistent@example.com";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visitService.getVisitsByPatientEmail(email));
        assertEquals("Patient not found", exception.getMessage());
    }
}