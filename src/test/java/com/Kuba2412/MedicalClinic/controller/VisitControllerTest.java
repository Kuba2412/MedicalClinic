package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.handler.exception.DoctorNotFoundException;
import com.Kuba2412.MedicalClinic.handler.exception.PatientNotFound;
import com.Kuba2412.MedicalClinic.handler.exception.VisitNotFound;
import com.Kuba2412.MedicalClinic.model.Patient;
import com.Kuba2412.MedicalClinic.model.Visit;
import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import com.Kuba2412.MedicalClinic.repository.PatientRepository;
import com.Kuba2412.MedicalClinic.repository.VisitRepository;
import com.Kuba2412.MedicalClinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private VisitRepository visitRepository;

    @MockBean
    private PatientRepository patientRepository;

    @Test
    void createVisit_ValidInput_VisitCreatedSuccessfully() throws Exception {
        // given
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setPatientId(1L);
        visitDTO.setStartVisit(LocalDateTime.now());
        visitDTO.setEndVisit(LocalDateTime.now().plusHours(1));

        when(visitService.createVisit(visitDTO)).thenReturn(visitDTO);

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L));
    }

    @Test
    void createVisit_InvalidInput_BadRequest() throws Exception {
        // given
        VisitDTO invalidVisitDTO = new VisitDTO();

        doThrow(new IllegalArgumentException("Visit can't be null."))
                .when(visitService).createVisit(invalidVisitDTO);

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVisitDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Visit can't be null."));
    }

    @Test
    void getAllVisitsForPatient_VisitsExist_VisitsReturned() throws Exception {
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

        VisitDTO visitDTO1 = new VisitDTO();
        visitDTO1.setId(1L);
        visitDTO1.setStartVisit(startDate1);
        visitDTO1.setEndVisit(endDate1);

        VisitDTO visitDTO2 = new VisitDTO();
        visitDTO2.setId(2L);
        visitDTO2.setStartVisit(startDate2);
        visitDTO2.setEndVisit(endDate2);

        List<VisitDTO> visitDTOs = Arrays.asList(visitDTO1, visitDTO2);
        when(visitService.getAllVisitsForPatient(patientId)).thenReturn(visitDTOs);

        // when + then
        mockMvc.perform(get("/visits/patient/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(visitDTOs.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAllVisitsForPatient_PatientNotFound_NotFound() throws Exception {
        // given
        Long nonExistentPatientId = 12345L;

        when(visitService.getAllVisitsForPatient(nonExistentPatientId))
                .thenThrow(new PatientNotFound("Patient not found."));

        // when + then
        mockMvc.perform(get("/visits/patient/{patientId}", nonExistentPatientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found."));
    }

    @Test
    void getVisitsByDoctorId_VisitsExist_VisitsReturned() throws Exception {
        Long doctorId = 1L;
        List<VisitDTO> visits = Arrays.asList(
                new VisitDTO(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, doctorId, "Cardiology"),
                new VisitDTO(2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, doctorId, "Cardiology")
        );

        when(visitService.getVisitsByDoctorId(doctorId)).thenReturn(visits);

        mockMvc.perform(get("/visits/doctor/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(visits.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getVisitsByDoctorId_DoctorNotFound_NotFound() throws Exception {
        Long doctorId = 1L;

        when(visitService.getVisitsByDoctorId(doctorId)).thenThrow(new DoctorNotFoundException("Doctor not found."));

        mockMvc.perform(get("/visits/doctor/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Doctor not found."));
    }

    @Test
    void getAvailableVisitsByDoctorSpecializationAndByDate_VisitsExist_VisitsReturned() throws Exception {
        String specialization = "Cardiology";
        LocalDate date = LocalDate.now();
        List<VisitDTO> visits = Arrays.asList(
                new VisitDTO(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, 2L, "specialization"),
                new VisitDTO(2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, 2L, "specialization")
        );

        when(visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date)).thenReturn(visits);

        mockMvc.perform(get("/visits/doctor/specialization/{specialization}/available-dates", specialization)
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(visits.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAvailableVisitsByDoctorSpecializationAndByDate_NoVisitsFound_NotFound() throws Exception {
        String specialization = "Cardiology";
        LocalDate date = LocalDate.now();


        when(visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date))
                .thenThrow(new VisitNotFound("Visit not found."));

        mockMvc.perform(get("/visits/doctor/specialization/{specialization}/available-dates", specialization)
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Visit not found."));
    }
}