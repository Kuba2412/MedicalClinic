package com.Kuba2412.MedicalClinic.repository;

import com.Kuba2412.MedicalClinic.model.Doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialization(String specialization);

    Optional<Doctor> findByEmail(String email);
}
