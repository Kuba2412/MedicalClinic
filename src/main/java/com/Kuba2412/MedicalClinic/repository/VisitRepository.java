package com.Kuba2412.MedicalClinic.repository;

import com.Kuba2412.MedicalClinic.model.Visit;

import com.Kuba2412.MedicalClinic.model.mapper.VisitMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, VisitMapper {

    List<Visit> findAllByPatientId(Long patientId);

    Page<Visit> findAllByDoctorIdAndPatientIsNull(Long doctorId, Pageable pageable);

    Page<Visit> findAllByDoctorSpecializationAndStartVisitBetweenAndPatientIsNull(
            String specialization, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
