package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.Patient;
import com.Kuba2412.MedicalClinic.model.dto.PatientDTO;
import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.Kuba2412.MedicalClinic.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{email}")
    @Operation(summary = "Get patient by email", description = "Retrieve a patient by their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found and returned"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public PatientDTO getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientDtoByEmail(email);
    }

    @GetMapping
    @Operation(summary = "Get patients by first name", description = "Retrieve a list of patients by their first name, with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patients returned successfully")
    })
    public List<PatientDTO> getPatientsByFirstName(@RequestParam String firstName, Pageable pageable) {
        return patientService.getPatientDtosByFirstName(firstName, pageable);
    }

    @PostMapping
    @Operation(summary = "Add a new patient", description = "Create a new patient record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete patient by email", description = "Delete a patient record by their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public void deletePatientByEmail(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update patient by email", description = "Update a patient's information by their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public PatientDTO updatePatientByEmail(@PathVariable("email") String email, @RequestBody PatientDTO patientDto) {
        return patientService.updatePatientByEmail(email, patientDto);
    }
}

