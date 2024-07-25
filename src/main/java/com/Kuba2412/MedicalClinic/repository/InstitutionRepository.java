package com.Kuba2412.MedicalClinic.repository;


import com.Kuba2412.MedicalClinic.model.Institution;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Page<Institution> findAll(Pageable pageable);
}
