package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.mapper.InstitutionMapper;
import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.InstitutionDTO;
import com.Kuba2412.MedicalClinic.repository.InstitutionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    /**
     * Test case 1: Poprawne dodanie instytucji

     * - Opis: Sprawdza, czy instytucja jest poprawnie tworzona, gdy dane są prawidłowe.
     * - Kroki:
     *   1. Utwórz obiekt z poprawnymi danymi dla InstitutionDTO.
     *   2. Wywołaj metodę createInstitution z utworzonym obiektem InstitutionDTO.
     *   3. Sprawdź czy instytucja została zapisana poprzez wywołanie save na institutionRepository.
     * - Wynik: Instytucja jest poprawnie zapisana w bazie danych.
     */

    public void createInstitution(InstitutionDTO institutionDTO) {
        if (institutionDTO == null) {
            throw new IllegalArgumentException("Institution can't be null.");
        }
        Institution institution = institutionMapper.toInstitution(institutionDTO);
        institutionRepository.save(institution);
    }

    /**
     * Test case 1: Pobranie wszystkich instytucji z paginacją

     * - Opis: Sprawdza, czy lista wszystkich instytucji jest zwracana zgodnie z paginacją.
     * - Kroki:
     *   1. Utwórz obiekt Pageable z ustawioną paginacją (np. PageRequest.of(0, 10)).
     *   2. Wywołaj metodę getAllInstitutions z utworzonym obiektem Pageable.
     *   3. Sprawdź czy lista instytucji zawiera oczekiwaną liczbę elementów (np. 10).
     * - Wynik: Lista instytucji jest poprawnie pobrana zgodnie z paginacją.
     */

    @Transactional
    public List<InstitutionDTO> getAllInstitutions(Pageable pageable) {
        List<Institution> institutions = institutionRepository.findAll(pageable).getContent();
        return institutions.stream()
                .map(institutionMapper::toInstitutionDTO)
                .toList();
    }

    /**
     * Test case 1: Pobranie lekarzy przypisanych do instytucji

     * - Opis: Sprawdza, czy lista lekarzy przypisanych do danej instytucji jest poprawnie zwracana.
     * - Kroki:
     *   1. Utwórz instytucję w bazie danych lub za pomocą innych metod testowych.
     *   2. Uzyskaj identyfikator tej instytucji.
     *   3. Wywołaj metodę getDoctorsForInstitution z tym identyfikatorem.
     *   4. Sprawdź czy lista lekarzy nie jest pusta i zawiera oczekiwaną liczbę elementów.
     * - Wynik: Lista lekarzy przypisanych do instytucji jest poprawnie zwrócona.
     */

    public List<Doctor> getDoctorsForInstitution(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        return institution.getDoctors();
    }
}