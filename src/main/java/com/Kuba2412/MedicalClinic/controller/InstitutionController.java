package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.dto.InstitutionDTO;
import com.Kuba2412.MedicalClinic.service.InstitutionService;

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
@RequestMapping("/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institution created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public String createInstitution(@RequestBody InstitutionDTO institutionDTO) {
        institutionService.createInstitution(institutionDTO);
        return "Institution created successfully.";
    }

    @GetMapping
    @Operation(summary = "Get all institutions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of institutions returned successfully")
    })
    public List<InstitutionDTO> getAllInstitutions(Pageable pageable) {
        return institutionService.getAllInstitutions(pageable);
    }

    @GetMapping("/{institutionId}/doctors")
    @Operation(summary = "Get doctors for an institution", description = "Retrieve a list of doctors assigned to a specific institution by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of doctors returned successfully"),
            @ApiResponse(responseCode = "404", description = "Institution not found")
    })
    public List<Doctor> getDoctorsForInstitution(@PathVariable Long institutionId) {
        return institutionService.getDoctorsForInstitution(institutionId);
    }
}