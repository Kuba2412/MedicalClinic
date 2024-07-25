package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.dto.InstitutionDTO;
import com.Kuba2412.MedicalClinic.service.InstitutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InstitutionService institutionService;

    private InstitutionDTO institutionDTO;
    private Doctor doctor;

    @BeforeEach
    void setup() {
        institutionDTO = new InstitutionDTO();
        institutionDTO.setName("Szpital 1");

        doctor = new Doctor();
        doctor.setFirstName("Kuba");
        doctor.setLastName("Ppp");
    }

    @Test
    void createInstitution_ValidInput_InstitutionCreated() throws Exception {
        mockMvc.perform(post("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(institutionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Institution created successfully."));
    }

    @Test
    void createInstitution_InvalidInput_BadRequest() throws Exception {
        InstitutionDTO invalidInstitutionDTO = new InstitutionDTO();

        mockMvc.perform(post("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInstitutionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid institution data."));
    }

    @Test
    void getAllInstitutions_InstitutionsExist_InstitutionsReturned() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<InstitutionDTO> institutions = Arrays.asList(institutionDTO);
        Page<InstitutionDTO> institutionPage = new PageImpl<>(institutions, pageable, institutions.size());
        when(institutionService.getAllInstitutions(any(Pageable.class))).thenReturn(institutions);

        mockMvc.perform(get("/institutions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(institutions.size()));
    }

    @Test
    void getDoctorsForInstitution_InstitutionExists_DoctorsReturned() throws Exception {
        List<Doctor> doctors = Arrays.asList(doctor);
        when(institutionService.getDoctorsForInstitution(anyLong())).thenReturn(doctors);

        mockMvc.perform(get("/institutions/1/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(doctors.size()));
    }

    @Test
    void getDoctorsForInstitution_InstitutionNotFound_ThrowException() throws Exception {
        when(institutionService.getDoctorsForInstitution(anyLong()))
                .thenThrow(new IllegalArgumentException("Institution not found."));

        mockMvc.perform(get("/institutions/12345/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Institution not found."));
    }
}