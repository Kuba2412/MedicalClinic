package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.mapper.SimpleDoctorDTO;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.DoctorDTO;
import com.Kuba2412.MedicalClinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public String createDoctor(@RequestBody DoctorDTO doctorDTO) {
        doctorService.createDoctor(doctorDTO);
        return "Doctor created successfully.";
    }

    @GetMapping
    @Operation(summary = "Get all doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of doctors returned successfully")
    })
    public List<SimpleDoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    @GetMapping("/simple")
    @Operation(summary = "Get all simple doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of simple doctors returned successfully")
    })
    public List<SimpleDoctorDTO> getAllSimpleDoctors(Pageable pageable) {
        return doctorService.getAllSimpleDoctors(pageable);
    }

    @GetMapping("/{doctorId}/institutions")
    @Operation(summary = "Get institutions assigned to a doctor", description = "Retrieve a list of institutions assigned to a specific doctor by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of institutions returned successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public List<Institution> getAssignedInstitutionsForDoctor(@PathVariable Long doctorId) {
        return doctorService.getAssignedInstitutionsForDoctor(doctorId);
    }
}