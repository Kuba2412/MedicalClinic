package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.handler.exception.PatientNotFound;
import com.Kuba2412.MedicalClinic.model.mapper.PatientMapper;
import com.Kuba2412.MedicalClinic.model.dto.PatientDTO;
import com.Kuba2412.MedicalClinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.Kuba2412.MedicalClinic.model.Patient;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;


    /**
     * Test case 1: Pobranie DTO pacjenta po adresie e-mail

     * - Opis: Sprawdza, czy metoda getPatientDtoByEmail poprawnie zwraca DTO pacjenta na podstawie adresu e-mail.
     * - Kroki:
     *   1. Utwórz obiekt pacjenta i zapisz go do bazy danych.
     *   2. Uzyskaj adres e-mail tego pacjenta.
     *   3. Wywołaj metodę getPatientDtoByEmail z tym adresem e-mail.
     *   4. Sprawdź czy zwrócone DTO pacjenta zawiera oczekiwane dane.
     * - Wynik: Metoda zwraca poprawne DTO pacjenta na podstawie adresu e-mail.
     */

    public PatientDTO getPatientDtoByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found"));
        return patientMapper.patientToPatientDTO(patient);
    }

    /**
     * Test case 1: Pobranie DTO pacjentów po pierwszym imieniu z paginacją

     * - Opis: Sprawdza, czy metoda getPatientDtosByFirstName poprawnie zwraca listę DTO pacjentów na podstawie pierwszego imienia z paginacją.
     * - Kroki:
     *   1. Utwórz kilku pacjentów z różnymi pierwszymi imionami i zapisz ich do bazy danych.
     *   2. Utwórz obiekt Pageable z ustawioną paginacją (np. PageRequest.of(0, 10)).
     *   3. Wywołaj metodę getPatientDtosByFirstName z pierwszym imieniem i Pageable.
     *   4. Sprawdź czy zwrócone DTO pacjentów zawierają oczekiwane dane i mają prawidłową paginację.
     * - Wynik: Metoda zwraca poprawną listę DTO pacjentów na podstawie pierwszego imienia z paginacją.
     */

    @Transactional
    public List<PatientDTO> getPatients(String firstName, Pageable pageable) {
        List<Patient> patients = firstName != null
                ? patientRepository.findByFirstName(firstName, pageable).getContent()
                : patientRepository.findAll(pageable).getContent();

        return patients.stream()
                .map(patientMapper::patientToPatientDTO)
                .toList();
    }

    /**
     * Test case 1: Dodanie nowego pacjenta

     * - Opis: Sprawdza, czy metoda addPatient poprawnie dodaje nowego pacjenta do bazy danych.
     * - Kroki:
     *   1. Utwórz obiekt pacjenta z poprawnymi danymi.
     *   2. Wywołaj metodę addPatient z tym obiektem pacjenta.
     *   3. Sprawdź czy pacjent został poprawnie zapisany w bazie danych.
     * - Wynik: Metoda zwraca zapisanego pacjenta z prawidłowymi danymi identyfikacyjnymi.
     */

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Test case: Usunięcie pacjenta po adresie e-mail

     * - Opis: Sprawdza, czy metoda deletePatientByEmail poprawnie usuwa pacjenta na podstawie adresu e-mail.
     * - Kroki:
     *   1. Utwórz pacjenta i zapisz go do bazy danych.
     *   2. Uzyskaj adres e-mail tego pacjenta.
     *   3. Wywołaj metodę deletePatientByEmail z tym adresem e-mail.
     *   4. Sprawdź czy pacjent został usunięty z bazy danych.
     * - Wynik: Metoda usuwa pacjenta na podstawie adresu e-mail.
     */

    public void deletePatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found."));
        patientRepository.delete(patient);
    }

    /**
     * Test case 1: Aktualizacja danych pacjenta po adresie e-mail

     * - Opis: Sprawdza, czy metoda updatePatientByEmail poprawnie aktualizuje dane pacjenta na podstawie adresu e-mail.
     * - Kroki:
     *   1. Utwórz pacjenta i zapisz go do bazy danych.
     *   2. Uzyskaj adres e-mail tego pacjenta oraz utwórz nowe dane dla pacjenta.
     *   3. Wywołaj metodę updatePatientByEmail z tym adresem e-mail i nowymi danymi pacjenta.
     *   4. Sprawdź czy pacjent został poprawnie zaktualizowany w bazie danych i czy zwrócone DTO zawiera oczekiwane dane.
     * - Wynik: Metoda zwraca zaktualizowane DTO pacjenta na podstawie adresu e-mail.
     */

    /**
     * Test case 2: Aktualizacja danych pacjenta po adresie e-mail (brak pacjenta w bazie danych)

     * - Opis: Sprawdza, czy metoda updatePatientByEmail rzuci wyjątek IllegalArgumentException,
     *         gdy pacjent o podanym adresie e-mail nie zostanie znaleziony w bazie danych.
     * - Kroki:
     *   1. Uzyskaj nieistniejący adres e-mail pacjenta oraz utwórz nowe dane dla pacjenta.
     *   2. Wywołaj metodę updatePatientByEmail z tym adresem e-mail i nowymi danymi pacjenta.
     *   3. Sprawdź czy metoda rzuciła wyjątek IllegalArgumentException.
     * - Wynik: Metoda rzuci wyjątek IllegalArgumentException, informujący o braku
     *                     pacjenta o podanym adresie e-mail w bazie danych.
     */

    @Transactional
    public PatientDTO updatePatientByEmail(String email, PatientDTO newPatientDto) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found."));

        if (newPatientDto.getFirstName() != null) {
            patient.setFirstName(newPatientDto.getFirstName());
        }
        if (newPatientDto.getLastName() != null) {
            patient.setLastName(newPatientDto.getLastName());
        }
        if (newPatientDto.getPhoneNumber() != null) {
            patient.setPhoneNumber(newPatientDto.getPhoneNumber());
        }
        if (newPatientDto.getEmail() != null) {
            patient.setEmail(newPatientDto.getEmail());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.patientToPatientDTO(updatedPatient);
    }
}