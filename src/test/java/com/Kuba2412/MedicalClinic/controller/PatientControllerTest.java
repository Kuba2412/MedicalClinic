//package com.Kuba2412.MedicalClinic.controller;
//
//import com.Kuba2412.MedicalClinic.handler.exception.PatientNotFound;
//import com.Kuba2412.MedicalClinic.model.Patient;
//import com.Kuba2412.MedicalClinic.model.dto.PatientDTO;
//import com.Kuba2412.MedicalClinic.service.PatientService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class PatientControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private PatientService patientService;
//
//    private Patient patient;
//    private PatientDTO patientDTO;
//
//    @BeforeEach
//    void setup() {
//        patient = new Patient();
//
//        patient.setEmail("kuba123@gmail.com");
//        patient.setFirstName("Kuba");
//        patient.setLastName("Ppp");
//
//        patientDTO = new PatientDTO();
//
//        patientDTO.setEmail("kuba123@gmail.com");
//        patientDTO.setFirstName("Kuba");
//        patientDTO.setLastName("Ppp");
//    }
//
//    @Test
//    void getPatientByEmail_PatientExsists_PatientReturned() throws Exception {
//
//        when(patientService.getPatientDtoByEmail(anyString())).thenReturn(patientDTO);
//
//        mockMvc.perform(get("/patients/kuba123@gmail.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value(patientDTO.getEmail()));
//    }
//
//    @Test
//    void getPatientByEmail_PatientNotFound_ThrowException() throws Exception {
//
//        when(patientService.getPatientDtoByEmail(anyString())).thenThrow(new PatientNotFound("Patient not found."));
//
//        mockMvc.perform(get("/patients/kuba@gmail.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Patient not found."));
//    }
//
//    @Test
//    void getPatientsByFirstName_PatientExsists_PatientReturned() throws Exception {
//        List<PatientDTO> patients = List.of(patientDTO);
//
//        when(patientService.getPatientDtosByFirstName(anyString(), any(Pageable.class))).thenReturn(patients);
//
//        mockMvc.perform(get("/patients/Kuba")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size").value(patients.size()));
//    }
//
//    @Test
//    void addPatient_ValidInput_PatientAdded() throws Exception {
//        when(patientService.addPatient(any(Patient.class))).thenReturn(patient);
//
//        mockMvc.perform(post("/patients")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(patient)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value(patient.getEmail()));
//    }
//
//    @Test
//    void deletePatientByEmail_PatientExists_PatientDeleted() throws Exception {
//        mockMvc.perform(delete("/patients/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void updatePatientByEmail_PatientExists_PatientUpdated() throws Exception {
//        when(patientService.updatePatientByEmail(anyString(), any(PatientDTO.class))).thenReturn(patientDTO);
//
//        mockMvc.perform(put("/patients/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(patientDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value(patientDTO.getEmail()));
//    }
//
//    @Test
//    void updatePatientByEmail_PatientNotFound_ThrowException() throws Exception {
//        when(patientService.updatePatientByEmail(anyString(), any(PatientDTO.class))).thenThrow(new PatientNotFound("Patient not found"));
//
//        mockMvc.perform(put("/patients/nonexistent@example.com")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(patientDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Patient not found"));
//    }
//}