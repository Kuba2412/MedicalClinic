package com.Kuba2412.MedicalClinic.controller;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.dto.SimpleDoctorDTO;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.DoctorDTO;
import com.Kuba2412.MedicalClinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Create a new doctor", description = "Creates a new doctor and returns a success message. Ensures the email is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Doctor.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or email is already exist",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createDoctor(@RequestBody DoctorDTO doctorDTO) {
        doctorService.createDoctor(doctorDTO);
        return "Doctor created successfully.";
    }

    @Operation(summary = "Get all doctors", description = "Returns a paginated list of all doctors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of doctors",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SimpleDoctorDTO.class ))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)
    })
    @GetMapping
    public List<SimpleDoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    @Operation(summary = "Get all simple doctors", description = "Returns a list of all doctors with basic information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of simple doctors",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SimpleDoctorDTO.class ))}),
    })
    @GetMapping("/simple")
    public List<SimpleDoctorDTO> getAllSimpleDoctors(Pageable pageable) {
        return doctorService.getAllSimpleDoctors(pageable);
    }

    @Operation(summary = "Get institutions assigned to a doctor", description = "Retrieve a list of institutions assigned to a specific doctor by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of institutions returned successfully",
                    content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Institution.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content)
    })
    @GetMapping("/{doctorId}/institutions")
    public List<Institution> getAssignedInstitutionsForDoctor(@PathVariable Long doctorId) {
        return doctorService.getAssignedInstitutionsForDoctor(doctorId);
    }
}