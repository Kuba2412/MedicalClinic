package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import com.Kuba2412.MedicalClinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "Register patient for a visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or registration not allowed"),
            @ApiResponse(responseCode = "404", description = "Visit or patient not found")
    })
    @PostMapping("/visits/{visitId}/patient/{patientId}/register")
    public void registerPatientForVisit(@PathVariable Long visitId, @RequestParam Long patientId) {
        visitService.registerPatientForVisit(visitId, patientId);
    }

    @Operation(summary = "Get visits by doctor ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/doctor/{doctorId}")
    public List<VisitDTO> getVisitsByDoctorId(@PathVariable Long doctorId) {
        return visitService.getVisitsByDoctorId(doctorId);
    }

    @Operation(summary = "Get all visits for a patient by patient ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{patientId}")
    public List<VisitDTO> getAllVisitsForPatient(@PathVariable Long patientId) {
        return visitService.getAllVisitsForPatient(patientId);
    }

    @Operation(summary = "Get available visits by doctor specialization and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available visits retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No available visits found for the specified criteria")
    })
    @GetMapping("/doctor/specialization/{specialization}/available-dates")
    public List<VisitDTO> getAvailableVisitsByDoctorSpecializationAndByDate(
            @PathVariable String specialization,
            @RequestParam LocalDate date) {
        return visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date);
    }

    @Operation(summary = "Create a new visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VisitDTO createVisit(@RequestBody VisitDTO visitDTO) {
        return visitService.createVisit(visitDTO);
    }

    @Operation(summary = "Get visits by patient email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/email/{email}")
    public List<VisitDTO> getVisitsByPatientEmail(@PathVariable String email) {
        return visitService.getVisitsByPatientEmail(email);
    }
}