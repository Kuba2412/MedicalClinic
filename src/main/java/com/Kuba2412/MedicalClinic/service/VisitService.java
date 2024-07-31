package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.handler.exception.DoctorNotFoundException;
import com.Kuba2412.MedicalClinic.handler.exception.PatientNotFound;
import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.mapper.VisitMapper;
import com.Kuba2412.MedicalClinic.model.Patient;
import com.Kuba2412.MedicalClinic.model.Visit;
import com.Kuba2412.MedicalClinic.model.dto.VisitDTO;
import com.Kuba2412.MedicalClinic.repository.DoctorRepository;
import com.Kuba2412.MedicalClinic.repository.PatientRepository;
import com.Kuba2412.MedicalClinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;

    @Transactional
    public VisitDTO createVisit(VisitDTO visitDTO) {
        Visit visit = visitMapper.visitDTOToVisit(visitDTO);
        Visit savedVisit = visitRepository.save(visit);
        return visitMapper.visitToVisitDTO(savedVisit);
    }

    public List<VisitDTO> getAllVisitsForPatient(Long patientId) {
        List<Visit> visits = visitRepository.findAllByPatientId(patientId);
        if (visits.isEmpty()) {
            throw new PatientNotFound("Patient not found.");
        }
        return visits.stream()
                .map(visitMapper::visitToVisitDTO)
                .toList();
    }

    public void registerPatientForVisit(Long visitId, Long patientId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));

        if (visit.getStartVisit().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for past visit.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFound("Patient not found"));

        if (visit.getPatient() != null) {
            throw new IllegalArgumentException("Visit already has registered patient.");
        }

        visit.setPatient(patient);
        visitRepository.save(visit);
    }

    public List<VisitDTO> getAvailableVisitsByDoctorSpecializationAndByDate(String specialization, LocalDate date) {
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        return doctors.stream()
                .flatMap(doctor -> doctor.getVisits().stream())
                .filter(visit -> visit.getStartVisit().toLocalDate().equals(date))
                .map(visitMapper::visitToVisitDTO)
                .toList();
    }

    public List<VisitDTO> getVisitsByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        return doctor.getVisits().stream()
                .map(visitMapper::visitToVisitDTO)
                .toList();
    }

    public List<VisitDTO> getVisitsByPatientEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found"));

        List<Visit> visits = visitRepository.findAllByPatientId(patient.getId());

        return visits.stream()
                .map(visitMapper::visitToVisitDTO)
                .toList();
    }

    private void validateVisitDateTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid visit start date.");
        }
        if (dateTime.getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Visit time must be in full quarter-hour intervals.");
        }
    }
}