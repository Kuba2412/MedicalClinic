package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.mapper.DoctorMapper;
import com.Kuba2412.MedicalClinic.model.mapper.SimpleDoctorDTO;
import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.DoctorDTO;
import com.Kuba2412.MedicalClinic.repository.DoctorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    /**
     * Test Case 1: Poprawne dane dla Doctor
     * - Opis: Sprawdza czy Doctor jest poprawnie tworzony, gdy dane są prawidłowe
     * - Kroki:
     * 1. Utwórz obiekt z poprawnymi danymi dla DoctorDTO
     * 2. Wywołaj metodę createDoctor z utworzonym obiektem DoctorDTO
     * 3. powiedz co ma zwrócic metoda toDctor z doctorMapper
     * 4. Sprawdź czy została wywołana metoda save z doctorRepository
     * - Wynik: Doctor jest poprawnie zapisany w bazie danych.
     **/

    @Transactional
    public void createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = doctorMapper.toDoctor(doctorDTO);
        doctorRepository.save(doctor);
    }

    /**
     * Test case 1: Pobieranie wszystkich lekarzy z paginacją
     * <p>
     * - Opis: Sprawdza, czy metoda getAllDoctors zwraca listę wszystkich lekarzy z paginacją.
     * - Kroki:
     * 1. Utwórz kilku lekarzy i dodaj ich do bazy danych.
     * 2. Utwórz obiekt Pageable z ustawioną paginacją (np. PageRequest.of(0, 10)).
     * 3. Wywołaj metodę getAllDoctors z obiektem Pageable.
     * 4. Sprawdź czy lista instytucji zawiera oczekiwaną liczbę elementów (np. 10).
     * - Wynik: Metoda zwraca poprawnie paginowaną listę wszystkich lekarzy w formacie SimpleDoctorDTO.
     */

    @Transactional
    public List<SimpleDoctorDTO> getAllDoctors(Pageable pageable) {
        List<Doctor> doctors = doctorRepository.findAll(pageable).getContent();
        return doctors.stream()
                .map(doctorMapper::toSimpleDoctorDTO)
                .toList();
    }

    /**
     * Test case 1: Pobieranie wszystkich prostych danych lekarzy z paginacją
     * <p>
     * - Opis: Sprawdza, czy metoda getAllSimpleDoctors zwraca listę wszystkich lekarzy w formacie SimpleDoctorDTO z paginacją.
     * - Kroki:
     * 1. Utwórz kilku lekarzy i dodaj ich do bazy danych.
     * 2. Utwórz obiekt Pageable z ustawioną paginacją (np. PageRequest.of(0, 10)).
     * 3. Wywołaj metodę getAllSimpleDoctors z obiektem Pageable.
     * 4. Sprawdź czy lista instytucji zawiera oczekiwaną liczbę elementów (np. 10).
     * - Wynik: Metoda zwraca poprawnie paginowaną listę prostych danych lekarzy.
     */

    public List<SimpleDoctorDTO> getAllSimpleDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toSimpleDoctorDTO)
                .getContent();
    }

    /**
     * Test Case 1: Poprawne ID lekarza dla getAssignedInstitutionsForDoctor
     * - Opis: Sprawdza poprawność pobierania przypisanych instytucji dla lekarza na podstawie jego identyfikatora.
     * - Kroki:
     *   1. Utwórz obiekt Doctor o znanym identyfikatorze.
     *   2. Wywołaj metodę getAssignedInstitutionsForDoctor z identyfikatorem utworzonego lekarza.
     *   3. Sprawdź czy metoda findById z doctorRepository została wywołana z odpowiednim identyfikatorem.
     *   4. Oczekuj, że zwrócona lista instytucji nie jest pusta.
     * - Wynik: Zwrócenie listy instytucji przypisanych do lekarza na podstawie poprawnego identyfikatora.
     */

    /**
     * Test case 2: Niepoprawne ID lekarza
     * <p>
     * - Opis: Sprawdza działanie metody getAssignedInstitutionsForDoctor na niepoprawny identyfikator lekarza.
     * - Kroki:
     * 1. Utwórz identyfikator lekarza, który nie istnieje w bazie danych.
     * 2. Kiedy zostanie wywołana metoda getAssignedInstitutionsForDoctor z niepoprawnym identyfikatorem lekarza,
     * oczekuj zgłoszenia wyjątku IllegalArgumentException.
     * 3. Sprawdź czy został rzucony wyjątek IllegalArgumentException.
     * 4. Upewnij się, że metoda findById z doctorRepository została wywołana z danym identyfikatorem.
     * - Wynik: Metoda powinna rzucać wyjątek, gdy identyfikator lekarza nie istnieje.
     */

    @Transactional
    public List<Institution> getAssignedInstitutionsForDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctor.getInstitutions();
    }
}