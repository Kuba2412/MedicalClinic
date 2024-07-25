package com.Kuba2412.MedicalClinic.repository;

import com.Kuba2412.MedicalClinic.model.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Page<Patient> findByFirstName(String firstName, Pageable pageable);

}
